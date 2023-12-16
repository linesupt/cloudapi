package com.lineying.common;

import java.util.Objects;

/**
 * 支付方式
 */
public enum PayType {
    NONE(0, "none"),
    WXPAY(10, "wxpay"),
    ALIPAY(11, "alipay"),
    PAYPAL(12, "paypal"),
    APPLE(13, "apple"),
    GOOGLE(14, "google");

    int id;
    String code;
    PayType(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static PayType get(String type) {
        if (Objects.isNull(type)) {
            return NONE;
        }
        switch (type) {
            case "none":
                return NONE;
            case "wxpay":
                return WXPAY;
            case "alipay":
                return ALIPAY;
            case "paypal":
                return PAYPAL;
            case "apple":
                return APPLE;
            case "google":
                return GOOGLE;
        }
        return ALIPAY;
    }

}
