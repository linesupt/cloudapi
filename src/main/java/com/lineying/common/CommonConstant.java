package com.lineying.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 通用常量
 *
 * @author ganjing
 */
@Component
public class CommonConstant {

    // 时间误差内允许请求
    public static long TIME_INTERVAL = 60 * 2;

    // 数据格式
    public static final String FORMAT = "json";
    // 订单超时时间
    public static final String TIMEOUT = "15m";
    // 签名方式
    public static final String SIGN_TYPE = "RSA2";
    // 支付宝网关,注意这些使用的是沙箱的支付宝网关，与正常网关的区别是多了dev
    public static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    public static final String GATEWAY_URL_DEV = "https://openapi.alipaydev.com/gateway.do";
    public static final String ALIPAY_NOTIFY_URL = "cloud/api/pay/alipay/notify";
    public static final String WXPAY_NOTIFY_URL = "cloud/api/pay/wxpay/notify";
    public static final String ALIPAY_NOTIFY_URL_V3 = "cloud/v3/pay/alipay/notify";
    public static final String WXPAY_NOTIFY_URL_V3 = "cloud/v3/pay/wxpay/notify";

    // 公钥，用于查找所属项目
    public static String BASE_URL;
    @Value("${config.base_url}")
    public void setDbApiKey(String baseUrl) {
        BASE_URL = baseUrl;
    }

}
