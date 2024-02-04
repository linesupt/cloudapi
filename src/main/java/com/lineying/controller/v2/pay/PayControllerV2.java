package com.lineying.controller.v2.pay;

import cn.hutool.core.io.FileUtil;
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
import com.lineying.common.SecureConfig;
import com.lineying.controller.BaseController;
import com.lineying.controller.api.pay.PayNotifyController;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonUpdateEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.*;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.app.model.Transaction;
import com.wechat.pay.java.service.payments.app.AppServiceExtension;
import com.wechat.pay.java.service.payments.app.model.Amount;
import com.wechat.pay.java.service.payments.app.model.CloseOrderRequest;
import com.wechat.pay.java.service.payments.app.model.PrepayRequest;
import com.wechat.pay.java.service.payments.app.model.PrepayWithRequestPaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import static com.lineying.common.CommonConstant.BASE_URL;
import static com.lineying.common.SignResult.KEY_ERROR;
import static com.lineying.common.SignResult.SIGN_ERROR;

/**
 * 应用级接口
 */
@Component
@RestController
@RequestMapping("v2")
public class PayControllerV2 extends BaseController {

    @Resource
    ICommonService commonService;

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
    public String alipayAppPay(HttpServletRequest request) {

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
        String app_id = jsonObject.get("app_id").getAsString();
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
        AlipayClient alipayClient = new DefaultAlipayClient(PayNotifyController.GATEWAY_URL, app_id, SecureConfig.ALIPAY_APP_PRI_KEY,
                FORMAT, CHARSET, SecureConfig.ALIPAY_PUB_KEY, PayNotifyController.SIGN_TYPE);
        // 实例化请求对象
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
        alipayRequest.setNotifyUrl(BASE_URL + PayNotifyController.ALIPAY_NOTIFY_URL);
        // 设置订单信息
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(outTradeNo);
        model.setSubject(body);
        model.setTotalAmount(total_fee);
        model.setBody(body);
        model.setTimeoutExpress(TIMEOUT);
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayRequest.setBizModel(model);
        try {
            AlipayTradeAppPayResponse resp = alipayClient.sdkExecute(alipayRequest);
            String respResult = resp.getBody();
            JsonObject resultObj = new JsonObject();
            resultObj.addProperty("trade_no", outTradeNo);
            resultObj.addProperty("order_info", respResult);
            return JsonCryptUtil.makeSuccess(resultObj);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
    }

    ////////////////////////// wechat pay ///////////////////////////////
    // 从 v0.2.10 开始，我们不再限制每个商户号只能创建一个 RSAAutoCertificateConfig。
    private RSAAutoCertificateConfig wxpayConfig;
    /** 创建配置 **/
    private RSAAutoCertificateConfig makeWxpayConfig() throws FileNotFoundException {
        if (wxpayConfig == null) {
            URL url = getClass().getClassLoader().getResource(SecureConfig.WXPAY_PRI_KEY_PATH);
            File file = new File(url.getFile());
            String keyString = FileUtil.readString(url, "utf-8");
            //File file = ResourceUtils.getFile("classpath:" + SecureConfig.WXPAY_PRI_KEY_PATH);
            //String path = file.getAbsolutePath();
            //LOGGER.info("私钥key::" + keyString);
            wxpayConfig = new RSAAutoCertificateConfig.Builder()
                    .merchantId(SecureConfig.WXPAY_MERCHANT_ID)
                    //.privateKeyFromPath(path)
                    .privateKey(keyString)
                    .merchantSerialNumber(SecureConfig.WXPAY_MERCHANT_SERIAL_NUMBER)
                    .apiV3Key(SecureConfig.WXPAY_APIV3_KEY)
                    .build();
        }
        return wxpayConfig;
    }

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

        // 生成商户自定义订单号
        int platform = Platform.get(request.getHeader("platform")).getId();
        String pay_type = jsonObject.get("pay_type").getAsString();
        String outTradeNo = PayType.get(pay_type).getId() + platform + TimeUtil.datetimeOrder(getCurrentTimeMs());

        int uid = jsonObject.get("uid").getAsInt();
        String appcode = jsonObject.get("appcode").getAsString();
        String goodsCode = jsonObject.get("goods_code").getAsString();
        String app_id = jsonObject.get("app_id").getAsString();
        String total_fee = jsonObject.get("total_fee").getAsString();
        String body = jsonObject.get("body").getAsString();
        // 单位为分
        int total = (int) Math.round(Double.parseDouble(total_fee) * 100);

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
        LOGGER.info("处理微信支付!" + app_id + " - " + outTradeNo + " - " + total_fee
                + " - " + body);
        // 构建service
        AppServiceExtension service = null;
        try {
            service = new AppServiceExtension.Builder()
                    .config(makeWxpayConfig()).build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        PrepayRequest prepayRequest = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(total);
        prepayRequest.setAmount(amount);
        prepayRequest.setAppid(app_id);
        prepayRequest.setMchid(SecureConfig.WXPAY_MERCHANT_ID);
        prepayRequest.setDescription(body);
        prepayRequest.setNotifyUrl(BASE_URL + PayNotifyController.WXPAY_NOTIFY_URL);
        prepayRequest.setOutTradeNo(outTradeNo);

        // 调用下单方法，得到应答
        PrepayWithRequestPaymentResponse response = service.prepayWithRequestPayment(prepayRequest);
        String appId = response.getAppid();
        String partnerId = response.getPartnerId();
        String prepayId = response.getPrepayId();
        String nonceStr = response.getNonceStr();
        String timestampRep = response.getTimestamp();
        String packageVal = response.getPackageVal();
        String sign = response.getSign();
        LOGGER.info("sign::" + sign);
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("out_trade_no", outTradeNo);
        resultObj.addProperty("appid", appId);
        resultObj.addProperty("partnerid", partnerId);
        resultObj.addProperty("prepayid", prepayId);
        resultObj.addProperty("noncestr", nonceStr);
        resultObj.addProperty("timestamp", timestampRep);
        resultObj.addProperty("package", packageVal);
        resultObj.addProperty("sign", sign);
        return JsonCryptUtil.makeSuccess(resultObj);
    }

}
