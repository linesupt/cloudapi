package com.lineying.controller.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.bean.Order;
import com.lineying.common.PayType;
import com.lineying.common.Platform;
import com.lineying.controller.BaseController;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonUpdateEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.AESUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.SignUtil;
import com.lineying.util.TimeUtil;
import com.wechat.pay.java.core.AbstractRSAConfig;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.app.model.Transaction;
import com.wechat.pay.java.service.payments.app.AppServiceExtension;
import com.wechat.pay.java.service.payments.app.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import static com.lineying.common.SignResult.KEY_ERROR;
import static com.lineying.common.SignResult.SIGN_ERROR;

/**
 * 应用级接口
 */
@Component
@RestController
@RequestMapping("api")
public class PayController extends BaseController {

    @Resource
    ICommonService commonService;

    @Value("${pay.app_pub_key}") // 应用公钥
    private String alipayAppPubKey;
    @Value("${pay.app_pri_key}") // 应用私钥
    private String alipayAppPriKey;
    @Value("${pay.alipay.pub_key}") // 支付宝公钥
    private String alipayPubKey;

    @Value("${pay.wxpay.merchant_id}") // 微信支付商户号
    private String wxpayMerchantId;
    @Value("${pay.wxpay.apiv3_key}") // 微信支付密钥
    private String wxpayApiv3Key;
    @Value("${pay.wxpay.private_key_path}") // 微信支付私钥路径
    private String wxpayPrivateKeyPath;
    @Value("${pay.wxpay.merchant_serial_number}") // 微信商户序列号
    private String wxpayMerchantSerialNumber;

    // 支付宝网关,注意这些使用的是沙箱的支付宝网关，与正常网关的区别是多了dev
    public static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    public static final String GATEWAY_URL_DEV = "https://openapi.alipaydev.com/gateway.do";
    public static final String BASE_URL = "http://api.lineying.cn/";
    public static final String ALIPAY_NOTIFY_PATH = BASE_URL + "cloud/api/pay/alipay/notify";
    // TODO 要求为https//...
    public static final String WXPAY_NOTIFY_PATH = BASE_URL + "cloud/api/pay/wxpay/notify";

    // 签名方式
    public static final String SIGN_TYPE = "RSA2";
    // 数据格式
    public static final String FORMAT = "json";
    // 订单超时时间
    public static final String TIMEOUT = "15m";



    /**
     * 创建支付宝支付信息
     *
     * @return
     */
    @RequestMapping("/pay/alipay/mkpay")
    public String alipayAppPay(HttpServletRequest request) throws AlipayApiException {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        // 生成订单号
        int platform = Platform.get(request.getHeader("platform")).getId();
        String pay_type = jsonObject.get("pay_type").getAsString();
        String outTradeNo = PayType.get(pay_type).getId() + platform + TimeUtil.datetimeOrder(getCurrentTimeMs());
        int uid = jsonObject.get("uid").getAsInt();
        String appcode = jsonObject.get("appcode").getAsString();
        String goodsCode = jsonObject.get("goods_code").getAsString();
        // 用于支付宝
        String app_id = jsonObject.get("app_id").getAsString();
        // 用于支付宝
        String total_fee = jsonObject.get("total_fee").getAsString();
        String body = jsonObject.get("body").getAsString();

        Order order = Order.makeOrder(uid, appcode, goodsCode, outTradeNo, body, pay_type);

        CommonAddEntity entity = new CommonAddEntity();
        entity.setTable(Order.TABLE);
        entity.setColumn(order.getColumn());
        entity.setValue(order.getValue());
        boolean result = false;
        try {
            // 保存订单
            result = commonService.add(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        if (!result) {
            return JsonCryptUtil.makeFail("create order fail");
        }

        Logger.getGlobal().info("处理支付宝支付!" + app_id + " - " + outTradeNo + " - " + total_fee + " - " + body);
        // 实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, app_id, alipayAppPriKey, FORMAT, CHARSET, alipayPubKey, SIGN_TYPE);
        // 实例化请求对象
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
        alipayRequest.setNotifyUrl(ALIPAY_NOTIFY_PATH);
        // 设置订单信息
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(outTradeNo);
        model.setSubject(body);
        model.setTotalAmount(total_fee);
        model.setBody(body);
        model.setTimeoutExpress(TIMEOUT);
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayRequest.setBizModel(model);
        AlipayTradeAppPayResponse resp = alipayClient.sdkExecute(alipayRequest);
        String respResult = resp.getBody();
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("trade_no", outTradeNo);
        resultObj.addProperty("order_info", respResult);
        return JsonCryptUtil.makeSuccess(resultObj);
    }

    /**
     * 支付宝支付通知
     *
     * @return
     */
    @RequestMapping("/pay/alipay/notify")
    public String alipayNotify(HttpServletRequest request)
            throws AlipayApiException {
        // 支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        Iterator<String> iterator = requestParams.keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayPubKey, CHARSET, SIGN_TYPE); //调用SDK验证签名
        if (signVerified) { // 验证成功
            // 商户订单号
            String out_trade_no = request.getParameter("out_trade_no");
            // 支付宝交易号
            String trade_no = request.getParameter("trade_no");
            // 交易状态
            String trade_status = request.getParameter("trade_status");
            Logger.getGlobal().info("处理支付宝通知!" + out_trade_no
                    + " - " + trade_no + " - " + trade_status);
            int status = 0;
            if (trade_status.equals("TRADE_FINISHED")) {
                // 判断该笔订单是否在商户网站中已经做过处理
                status = 1;
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                status = 1;
            }

            CommonUpdateEntity entity = new CommonUpdateEntity();
            entity.setSet(String.format("trade_no='%s', status='%s', update_time='%s'", trade_no, status + "", getCurrentTimeMs()));
            entity.setWhere(String.format("out_trade_no=%s", out_trade_no));
            entity.setTable(Order.TABLE);
            boolean result = false;
            try {
                result = commonService.update(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 处理订单数据保存
            return "success";
        }

        return "fail";
    }

    @RequestMapping("/pay/alipay/return")
    public String alipayReturn(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 解码
            params.put(name, URLDecoder.decode(valueStr, "UTF-8"));
        }
        // 验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayPubKey, CHARSET, SIGN_TYPE);
        if (signVerified) {
            return "success";
        } else {
            return "fail";
        }
    }

    ////////////////////////// wechat pay ///////////////////////////////

    /**
     * 创建微信支付信息（APP）
     * @return 返回客户端唤醒微信支付的方法
     */
    @RequestMapping("/pay/wxpay/mkpay")
    public String wxpayAppPay(HttpServletRequest request) {
        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        // 生成订单号
        int platform = Platform.get(request.getHeader("platform")).getId();
        String pay_type = jsonObject.get("pay_type").getAsString();
        String app_id = jsonObject.get("app_id").getAsString();
        String total_fee = jsonObject.get("total_fee").getAsString();
        String body = jsonObject.get("body").getAsString();
        // 单位为分
        int total = (int) (Math.round(Double.parseDouble(total_fee)) * 100);

        String outTradeNo = PayType.get(pay_type).getId() + platform + TimeUtil.datetimeOrder(getCurrentTimeMs());

        LOGGER.info("处理微信支付!" + app_id + " - " + outTradeNo + " - " + total_fee
                + " - " + body);

        // 使用自动更新平台证书的RSA配置

        // 构建service
        AppServiceExtension service = new AppServiceExtension.Builder()
                .config(makeWxpayConfig()).build();
        PrepayRequest prepayRequest = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(total);
        prepayRequest.setAmount(amount);
        prepayRequest.setAppid(app_id);
        prepayRequest.setMchid(wxpayMerchantId);
        prepayRequest.setDescription(body);
        prepayRequest.setNotifyUrl(WXPAY_NOTIFY_PATH);
        prepayRequest.setOutTradeNo(outTradeNo);
        // 调用下单方法，得到应答
        PrepayWithRequestPaymentResponse response = service.prepayWithRequestPayment(prepayRequest);
        // 获取prepayid
        String prepayId = response.getPrepayId();
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("prepay_id", prepayId);
        return JsonCryptUtil.makeSuccess(resultObj);
    }

    /** 创建配置 **/
    private RSAAutoCertificateConfig makeWxpayConfig() {
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        RSAAutoCertificateConfig config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(wxpayMerchantId)
                        .privateKeyFromPath(wxpayPrivateKeyPath)
                        .merchantSerialNumber(wxpayMerchantSerialNumber)
                        .apiV3Key(wxpayApiv3Key)
                        .build();
        return config;
    }

    /**
     * 微信支付通知
     *
     * @return
     */
    @RequestMapping("/pay/wxpay/notify")
    public String wxpayNotify(HttpServletRequest request) {
        // TODO 微信通知
        LOGGER.info("处理微信通知!");
        String characterEncoding = request.getCharacterEncoding();
        System.out.println("characterEncoding=" + characterEncoding);
        //从请求头获取验签字段
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String signature = request.getHeader("Wechatpay-Signature");
        String serial = request.getHeader("Wechatpay-Serial");

        String requestBody = "";
        HashMap<String, String> map = new HashMap<>();
        // 构造 RequestParam
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(serial)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .body(requestBody)
                .build();
        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(makeWxpayConfig());
        try {
            // 以支付通知回调为例，验签、解密并转换成 Transaction
            Transaction transaction = parser.parse(requestParam, Transaction.class);
        } catch (ValidationException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            LOGGER.error("sign verification failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).toString();
        }

        // 如果处理失败，应返回 4xx/5xx 的状态码，例如 500 INTERNAL_SERVER_ERROR
        if (false) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).toString();
        }

        // 处理成功，返回 200 OK 状态码
        return ResponseEntity.status(HttpStatus.OK).toString();
    }

    /**
     * 关闭订单
     * @param outTradeNo
     * @return
     */
    public String closeOrder(String outTradeNo) {
        CloseOrderRequest closeRequest = new CloseOrderRequest();
        closeRequest.setMchid(wxpayMerchantId);
        closeRequest.setOutTradeNo(outTradeNo);
        // 方法没有返回值，意味着成功时API返回204 No Content
        // 关闭订单
        // TODO service.closeOrder(closeRequest);
        return JsonCryptUtil.makeSuccess();
    }

    // 发起退款
    public String refund() {
        return "";
    }

}
