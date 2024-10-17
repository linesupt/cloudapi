package com.lineying.controller.api.pay;

/**
 * Apple通知状态
 */
public @interface AppleStatus {

    /**
     * 自动续订订阅已激活。
     */
    int ACTIVE = 1;

    /**
     * 自动续订订阅已过期。
     */
    int EXPIRED = 2;

    /**
     * 自动续费订阅正处于计费重试期内。
     */
    int BILLING_RETRY = 3;

    /**
     * 自动续订订阅处于计费宽限期内。
     */
    int BILLING_GRACE_PERIOD = 4;

    /**
     * 自动续订订阅被撤销。
     */
    int REVOKED = 5;

}
