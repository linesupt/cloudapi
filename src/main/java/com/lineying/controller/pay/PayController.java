package com.lineying.controller.pay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * 应用级接口
 */
@Component
@RestController
@RequestMapping("api")
public class PayController {

    @Value("${pay.app_pub_key}") // 应用公钥
    private String appPubKey;

    @Value("${pay.alipay.pub_key}") // 支付宝公钥
    private String alipayPubKey;

    /**
     * 创建支付宝支付信息
     * @return
     */
    @RequestMapping("/pay/alipay/appPay")
    public String alipayAppPay(HttpServletRequest request) {

        String out_trade_no = request.getParameter("out_trade_no");
        String total_fee = request.getParameter("total_fee");
        String mch_id = request.getParameter("mch_id");
        String body = request.getParameter("body");
        String sign = request.getParameter("sign");

        Logger.getGlobal().info("处理支付宝支付!" + out_trade_no + " - " + total_fee
                + " - " + mch_id + " - " + body + " - " + sign);

        return "alipay app pay";
    }

    /**
     * 支付宝支付通知
     * @return
     */
    @RequestMapping("/pay/alipay/notify")
    public String alipayNotify(HttpServletRequest request) {
        // TODO 支付宝通知
        Logger.getGlobal().info("处理支付宝通知!");

        return "alipay notify";
    }

    /**
     * 创建微信支付信息
     * @return
     */
    @RequestMapping("/pay/wxpay/appPay")
    public String wxpayAppPay(HttpServletRequest request) {
        String app_id = request.getParameter("app_id");
        String out_trade_no = request.getParameter("out_trade_no");
        String total_fee = request.getParameter("total_fee");
        String mch_id = request.getParameter("mch_id");
        String body = request.getParameter("body");
        String sign = request.getParameter("sign");

        Logger.getGlobal().info("处理微信支付!" + app_id + " - " + out_trade_no + " - " + total_fee
                        + " - " + mch_id + " - " + body + " - " + sign);
        return "wxpay app pay";
    }

    /**
     * 微信支付通知
     * @return
     */
    @RequestMapping("/pay/wxpay/notify")
    public String wxpayNotify(HttpServletRequest request) {
        // TODO 微信通知
        Logger.getGlobal().info("处理微信通知!");
        return "wxpay notify";
    }

}
