package com.lineying.controller.api.pay;

/**
 * Apple付费通知子类型
 */
public @interface AppleSubtype {

    /**
     * 适用于PRICE_INCREASE. 如果价格上涨需要客户同意，则带有此通知的通知表明客户同意订阅价格上涨；如果价格上涨不需要客户同意，则表明系统通知他们价格上涨。
     */
    String ACCEPTED = "ACCEPTED";

    /**
     * 适用于DID_CHANGE_RENEWAL_STATUS. 此类通知表明用户禁用了订阅自动续订，或者 App Store 在用户申请退款后禁用了订阅自动续订。
     */
    String AUTO_RENEW_DISABLED = "AUTO_RENEW_DISABLED";

    /**
     * 适用于DID_CHANGE_RENEWAL_STATUS. 包含此信息的通知表明用户启用了订阅自动续订。
     */
    String AUTO_RENEW_ENABLED = "AUTO_RENEW_ENABLED";

    /**
     * 适用于DID_RENEW. 出现此通知表示之前未能续订的过期订阅已成功续订。
     */
    String BILLING_RECOVERY = "BILLING_RECOVERY";

    /**
     * 适用于EXPIRED. 此类通知表明订阅已过期，因为订阅在计费重试期结束之前未能续订。
     */
    String BILLING_RETRY = "BILLING_RETRY";

    /**
     * 适用于DID_CHANGE_RENEWAL_PREF. 包含此信息的通知表明用户降级了其订阅或交叉分级为具有不同持续时间的订阅。降级将在下一个续订日期生效。
     */
    String DOWNGRADE = "DOWNGRADE";

    /**
     * 适用于RENEWAL_EXTENSION. 包含此信息的通知表明单个订阅的订阅续订日期延期失败。有关详细信息，请参阅中的对象。有关请求的信息，请参阅延长所有活跃订阅者的订阅续订日期。
     */
    String FAILURE = "FAILURE";

    /**
     * 适用于DID_FAIL_TO_RENEW. 包含此信息的通知表明订阅由于计费问题而无法续订。在宽限期内继续提供对订阅的访问。
     */
    String GRACE_PERIOD = "GRACE_PERIOD";

    /**
     * 适用于SUBSCRIBED. 包含此内容的通知表示用户首次购买订阅或用户首次通过家人共享获得对订阅的访问权限。
     */
    String INITIAL_BUY = "INITIAL_BUY";

    /**
     * 适用于PRICE_INCREASE. 出现此通知表示系统已通知用户订阅价格上涨，但用户尚未接受。
     */
    String PENDING = "PENDING";

    /**
     * 适用于EXPIRED. 此类通知表明订阅已过期，因为用户不同意涨价。
     */
    String PRICE_INCREASE = "PRICE_INCREASE";

    /**
     * 适用于EXPIRED. 包含此内容的通知表明订阅已过期，因为在订阅尝试续订时无法购买该产品。
     */
    String PRODUCT_NOT_FOR_SALE = "PRODUCT_NOT_FOR_SALE";

    /**
     * 适用于SUBSCRIBED. 带有此信息的通知表明用户通过家庭共享重新订阅或接收了对同一订阅或同一订阅组内的另一个订阅的访问权限。
     */
    String RESUBSCRIBE = "RESUBSCRIBE";

    /**
     * 适用于RENEWAL_EXTENSION. 此通知表明 App Store 服务器已完成您为所有符合条件的订阅者延长订阅续订日期的请求。有关摘要详细信息，请参阅中的对象。有关请求的信息，请参阅延长所有活跃订阅者的订阅续订日期。
     * notificationTypesubtypesummaryresponseBodyV2DecodedPayload
     */
    String SUMMARY = "SUMMARY";

    /**
     * 适用于DID_CHANGE_RENEWAL_PREF. 包含此信息的通知表明用户已升级其订阅或交叉分级为具有相同持续时间的订阅。升级立即生效。
     */
    String UPGRADE = "UPGRADE";

    /**
     * 适用于EXPIRED. 此类通知表明订阅在用户禁用订阅自动续订后已过期。
     */
    String VOLUNTARY = "VOLUNTARY";

}
