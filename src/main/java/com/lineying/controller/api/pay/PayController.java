package com.lineying.controller.api.pay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.google.gson.JsonObject;
import com.lineying.bean.Order;
import com.lineying.common.CommonConstant;
import com.lineying.common.SecureConfig;
import com.lineying.controller.Checker;
import com.lineying.data.Column;
import com.lineying.util.*;
import com.wechat.pay.java.service.payments.app.AppServiceExtension;
import com.wechat.pay.java.service.payments.app.model.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.logging.Logger;

/**
 * 应用级接口
 */
@Component
@RestController
@RequestMapping("api")
public class PayController extends BasePayController {

    /**
     * 创建支付宝支付信息
     * @return
     */
    @RequestMapping("/pay/alipay/mkpay")
    public String alipayAppPay(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        Order order = makeOrder(request);
        if (order == null) {
            return JsonCryptUtil.makeFail("create order fail");
        }
        LOGGER.info("处理支付宝支付!" + order);
        // 实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(CommonConstant.GATEWAY_URL, order.getAppid(), SecureConfig.ALIPAY_APP_PRI_KEY,
                CommonConstant.FORMAT, CommonConstant.CHARSET, SecureConfig.ALIPAY_PUB_KEY, CommonConstant.SIGN_TYPE);
        // 实例化请求对象
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
        LOGGER.info("设置alipay通知路径::" + getAlipayNotifyUrl());
        alipayRequest.setNotifyUrl(getAlipayNotifyUrl());
        // 设置订单信息
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(order.getOutTradeNo());
        model.setSubject(order.getBody());
        model.setTotalAmount(order.getTotalFee());
        model.setBody(order.getBody());
        model.setTimeoutExpress(CommonConstant.TIMEOUT);
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayRequest.setBizModel(model);
        try {
            AlipayTradeAppPayResponse resp = alipayClient.sdkExecute(alipayRequest);
            String respResult = resp.getBody();
            JsonObject resultObj = new JsonObject();
            resultObj.addProperty(Column.TRADE_NO, order.getOutTradeNo());
            resultObj.addProperty(Column.ORDER_INFO, respResult);
            return JsonCryptUtil.makeSuccess(resultObj);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
    }

    /**
     * 创建微信支付信息（APP）
     * @return 返回客户端唤醒微信支付的方法
     */
    @RequestMapping("/pay/wxpay/mkpay")
    public String wxpayAppPay(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        Order order = makeOrder(request);
        if (order == null) {
            return JsonCryptUtil.makeFail("create order fail");
        }
        LOGGER.info("处理微信支付!" + order);
        // 单位为分
        int total = (int) Math.round(Double.parseDouble(order.getTotalFee()) * 100);
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
        prepayRequest.setAppid(order.getAppid());
        prepayRequest.setMchid(SecureConfig.WXPAY_MERCHANT_ID);
        prepayRequest.setDescription(order.getBody());
        prepayRequest.setNotifyUrl(getWxpayNotifyUrl());
        LOGGER.info("设置wxpay通知路径::" + getWxpayNotifyUrl());
        prepayRequest.setOutTradeNo(order.getOutTradeNo());

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
        resultObj.addProperty(Column.OUT_TRADE_NO, order.getOutTradeNo());
        resultObj.addProperty(Column.APPID, appId);
        resultObj.addProperty(Column.PARTNER_ID, partnerId);
        resultObj.addProperty(Column.PREPAY_ID, prepayId);
        resultObj.addProperty(Column.NONCESTR, nonceStr);
        resultObj.addProperty(Column.TIMESTAMP, timestampRep);
        resultObj.addProperty(Column.PACKAGE, packageVal);
        resultObj.addProperty(Column.SIGN, sign);
        return JsonCryptUtil.makeSuccess(resultObj);
    }

}
