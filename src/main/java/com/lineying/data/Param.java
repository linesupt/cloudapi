package com.lineying.data;

/**
 * json参数或结果
 */
public @interface Param {

    @interface Key {

        String CODE = "code";
        String MSG = "msg";
        String DATA = "data";
        String TOKEN = "token";
        String STATUS = "status";
        String MESSAGE = "message";
        String PLATFORM = "platform";
        String LOCALE = "locale";
        String KEY = "key";
        String TIMESTAMP = "timestamp";
        String SIGNATURE = "signature";
    }

    @interface Result {

        String SUCCESS = "success";
        String FAIL = "fail";
        String CAP_SUCCESS = "SUCCESS";
        String CAP_FAIL = "FAIL";
    }

    @interface Wechatpay {

        //从请求头获取验签字段
        String TIMESTAMP = "Wechatpay-Timestamp";
        // 随机数
        String NONCE = "Wechatpay-Nonce";
        // 微信签名
        String SIGNATURE = "Wechatpay-Signature";
        // 证书序列号、多个证书的情况下用于查询对应的证书
        String SERIAL = "Wechatpay-Serial";
        // 签名方式
        String SIGNATURE_TYPE = "Wechatpay-Signature-Type";
    }

    @interface Trade {
        String FINISHED = "TRADE_FINISHED";
        String SUCCESS = "TRADE_SUCCESS";
    }

}
