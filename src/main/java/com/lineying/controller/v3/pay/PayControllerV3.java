package com.lineying.controller.v3.pay;

import com.lineying.common.CommonConstant;
import com.lineying.controller.v2.pay.PayControllerV2;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.lineying.common.CommonConstant.BASE_URL;

/**
 * 应用级接口
 * 订单生成在同一张表里
 * 自动处理过期时间，无需客户端再次同步
 */
@Component
@RestController
@RequestMapping("v3")
public class PayControllerV3 extends PayControllerV2 {

    @Override
    protected String getAlipayNotifyUrl() {
        return BASE_URL + CommonConstant.ALIPAY_NOTIFY_URL_V3;
    }

    @Override
    protected String getWxpayNotifyUrl() {
        return BASE_URL + CommonConstant.WXPAY_NOTIFY_URL_V3;
    }

    /**
     * 创建支付宝支付信息
     *
     * @return
     */
    @RequestMapping("/pay/alipay/mkpay")
    public String alipayAppPay(HttpServletRequest request) {
        return super.alipayAppPay(request);
    }

    /**
     * 创建微信支付信息（APP）
     * @return 返回客户端唤醒微信支付的方法
     */
    @RequestMapping("/pay/wxpay/mkpay")
    public String wxpayAppPay(HttpServletRequest request) {
        return super.wxpayAppPay(request);
    }

}
