package com.lineying.controller.pay;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * 应用级接口
 */
@RestController
@RequestMapping("api")
public class PayController {

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

        return "";
    }

    /**
     * 支付宝支付通知
     * @return
     */
    @RequestMapping("/pay/alipay/notify")
    public void alipayNotify(HttpServletRequest request) {
        // TODO 支付宝通知
        Logger.getGlobal().info("处理支付宝通知!");
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
        return "";
    }

    /**
     * 微信支付通知
     * @return
     */
    @RequestMapping("/pay/wxpay/notify")
    public void wxpayNotify(HttpServletRequest request) {
        // TODO 微信通知
        Logger.getGlobal().info("处理微信通知!");
    }

}
