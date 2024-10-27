package com.lineying.common;

/**
 * 交易状态
 */
public @interface TradeStatus {

    /**
     * 未处理
     */
    int NONE = 0;

    /**
     * 完成支付
     */
    int OK = 1;

    /**
     * 完成退款
     */
    int REFUND = 2;
}
