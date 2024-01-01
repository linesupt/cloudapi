package com.lineying.controller.pay;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
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
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        // 生成订单号
        int platform = Platform.get(request.getHeader("platform")).getId();
        String pay_type = jsonObject.getString("pay_type");
        String outTradeNo = PayType.get(pay_type).getId() + platform + TimeUtil.datetimeOrder(getCurrentTimeMs());
        int uid = jsonObject.getIntValue("uid");
        String appcode = jsonObject.getString("appcode");
        String goodsCode = jsonObject.getString("goods_code");
        // 用于支付宝
        String app_id = jsonObject.getString("app_id");
        // 用于支付宝
        String total_fee = jsonObject.getString("total_fee");
        String body = jsonObject.getString("body");

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
        JSONObject resultObj = new JSONObject();
        resultObj.put("trade_no", outTradeNo);
        resultObj.put("order_info", respResult);
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
     * 创建微信支付信息
     *
     * @return
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
        JSONObject jsonObject = JSON.parseObject(data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String app_id = jsonObject.getString("app_id");
        String out_trade_no = jsonObject.getString("out_trade_no");
        String total_fee = jsonObject.getString("total_fee");
        String body = jsonObject.getString("body");

        Logger.getGlobal().info("处理微信支付!" + app_id + " - " + out_trade_no + " - " + total_fee
                + " - " + body);


        return "wxpay app pay";
    }

    private void req(String outTradeNo) {
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(wxpayMerchantId)
                        .privateKeyFromPath(wxpayPrivateKeyPath)
                        .merchantSerialNumber(wxpayMerchantSerialNumber)
                        .apiV3Key(wxpayApiv3Key)
                        .build();
        // 构建service
        NativePayService service = new NativePayService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(100);
        request.setAmount(amount);
        request.setAppid("wxa9d9651ae******");
        request.setMchid("190000****");
        request.setDescription("测试商品标题");
        request.setNotifyUrl(WXPAY_NOTIFY_PATH);
        request.setOutTradeNo(outTradeNo);
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getCodeUrl());
    }

    /**
     * 微信支付通知
     *
     * @return
     */
    @RequestMapping("/pay/wxpay/notify")
    public String wxpayNotify(HttpServletRequest request) {
        // TODO 微信通知
        Logger.getGlobal().info("处理微信通知!");
        return "wxpay notify";
    }

}
