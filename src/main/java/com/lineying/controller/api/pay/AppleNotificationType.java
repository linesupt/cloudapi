package com.lineying.controller.api.pay;

/**
 * 苹果订阅支付通知类型
 */
public @interface AppleNotificationType {

    /**
     * 表示客户的订阅已成功自动续订了新的交易期
     */
    String DID_RENEW = "DID_RENEW";

    /**
     * 表示Apple客户支持取消了订阅或用户升级了订阅。 cancel_date键包含更改的日期和时间
     */
    String CANCEL = "CANCEL";

    /**
     * 指示客户对其订购计划进行了更改，该更改在下一次续订时生效。当前有效的计划不受影响
     */
    String DID_CHANGE_RENEWAL_PREF = "DID_CHANGE_RENEWAL_PREF";

    /**
     * 指示订阅续订状态的更改。在JSON响应中，检查auto_renew_status_change_date_ms以了解上一次状态更新的日期和时间。检查auto_renew_status以了解当前的续订状态
     */
    String DID_CHANGE_RENEWAL_STATUS = "DID_CHANGE_RENEWAL_STATUS";

    /**
     * 表示由于计费问题而无法续订的订阅。检查is_in_billing_retry_period以了解订阅的当前重试状态。如果订阅处于计费宽限期内，请检查grace_period_expires_date以了解新服务的到期日期
     */
    String DID_FAIL_TO_RENEW = "DID_FAIL_TO_RENEW";

    /**
     * 表示成功的自动更新已过期的订阅，而该订阅过去无法更新。检查expires_date，以确定下一个续订日期和时间
     */
    String DID_RECOVER = "DID_RECOVER";

    /**
     * 在用户最初购买订阅时发生。通过在App Store上对其进行身份验证，可以将Latest_receipt作为令牌存储在服务器上，以随时验证用户的订阅状态
     */
    String INITIAL_BUY = "INITIAL_BUY";

    /**
     * 指示客户使用您的应用程序界面或在该帐户的“订阅”设置中的App Store上以交互方式续订了订阅。立即提供服务
     */
    String INTERACTIVE_RENEWAL = "INTERACTIVE_RENEWAL";

    /**
     * 表示App Store已开始要求客户同意您的应用的订阅价格上涨。在Unified_receipt.Pending_renewal_info对象中，price_consent_status值为0，表示App Store正在征求客户的同意，但尚未收到。除非用户同意新价格，否则订阅不会自动续订。当客户同意提价时，系统将price_consent_status设置为1。使用verifyReceipt检查收货以查看更新的价格同意状态
     */
    String PRICE_INCREASE_CONSENT = "PRICE_INCREASE_CONSENT";

    /**
     * 表示App Store成功退还了一笔交易。 cancel_date_ms包含已退款交易的时间戳。 original_transaction_id和product_id标识原始交易和产品。 cancel_reason包含原因
     */
    String REFUND = "REFUND";

    /**
     * （在沙盒中弃用）
     * 表示成功的自动更新已过期的订阅，而该订阅过去无法更新。检查expires_date，以确定下一个续订日期和时间。此通知在沙箱环境中已弃用，计划于2021年3月在生产中弃用。更新现有代码以改为依赖DID_RECOVER通知类型
     */
    @Deprecated
    String RENEWAL = "RENEWAL";

    /**
     * 表示用户不再可以通过“家庭共享”获得应用内购买。当购买者禁用产品的家庭共享，购买者（或家庭成员）离开家庭组或购买者要求并收到退款时，StoreKit会发送此通知。您的应用程序还将收到PaymentQueue（_：didRevokeEntitlementsForProductIdentifiers :)调用。有关家庭共享的更多信息，请参阅在应用程序中支持家庭共享
     */
    String REVOKE = "REVOKE";

    /**
     * 一次性收费
     */
    String ONE_TIME_CHARGE = "ONE_TIME_CHARGE";

}
