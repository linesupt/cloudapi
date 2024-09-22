package com.lineying.controller.api.pay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * apple内购项
 * {
 *  "quantity": "1",
 *  "product_id": "cal_vip_monthly",
 *  "transaction_id": "2000000577512269",
 *  "original_transaction_id": "2000000577510234",
 *  "purchase_date": "2024-04-21 07:59:52 Etc/GMT",
 *  "purchase_date_ms": "1713686392000",
 *  "purchase_date_pst": "2024-04-21 00:59:52 America/Los_Angeles",
 *  "original_purchase_date": "2024-04-21 07:44:52 Etc/GMT",
 *  "original_purchase_date_ms": "1713685492000",
 *  "original_purchase_date_pst": "2024-04-21 00:44:52 America/Los_Angeles",
 *  "expires_date": "2024-04-21 08:04:52 Etc/GMT",
 *  "expires_date_ms": "1713686692000",
 *  "expires_date_pst": "2024-04-21 01:04:52 America/Los_Angeles",
 *  "web_order_line_item_id": "2000000058472661",
 *  "is_trial_period": "false",
 *  "is_in_intro_offer_period": "false",
 *  "in_app_ownership_type": "PURCHASED",
 *  "subscription_group_identifier": "20889266"
 * }
 */
public class InAppItem implements Comparable {

    @Expose
    @SerializedName("quantity")
    private int quantity;
    @Expose
    @SerializedName("product_id")
    private String productId;
    @Expose
    @SerializedName("transaction_id")
    private String transactionId;
    @Expose
    @SerializedName("original_transaction_id")
    private String originalTransactionId;
    @Expose
    @SerializedName("purchase_date")
    private String purchaseDate;
    @Expose
    @SerializedName("purchase_date_ms")
    private long purchaseDateMs;
    @Expose
    @SerializedName("purchase_date_pst")
    private String purchaseDatePst;
    @Expose
    @SerializedName("original_purchase_date")
    private String originalPurchaseDate;
    @Expose
    @SerializedName("original_purchase_date_ms")
    private String originalPurchaseDateMs;
    @Expose
    @SerializedName("original_purchase_date_pst")
    private String originalPurchaseDatePst;
    @Expose
    @SerializedName("expires_date")
    private String expiresDate;
    @Expose
    @SerializedName("expires_date_ms")
    private String expiresDateMs;
    @Expose
    @SerializedName("expires_date_pst")
    private String expiresDatePst;
    @Expose
    @SerializedName("web_order_line_item_id")
    private String webOrderLineItemId;
    @Expose
    @SerializedName("is_trial_period")
    private String isTrialPeriod;
    @Expose
    @SerializedName("is_in_intro_offer_period")
    private String isInIntroOfferPeriod;
    @Expose
    @SerializedName("in_app_ownership_type")
    private String inAppOwnershipType;
    @Expose
    @SerializedName("subscription_group_identifier")
    private String subscriptionGroupIdentifier;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOriginalTransactionId() {
        return originalTransactionId;
    }

    public void setOriginalTransactionId(String originalTransactionId) {
        this.originalTransactionId = originalTransactionId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public long getPurchaseDateMs() {
        return purchaseDateMs;
    }

    public void setPurchaseDateMs(long purchaseDateMs) {
        this.purchaseDateMs = purchaseDateMs;
    }

    public String getPurchaseDatePst() {
        return purchaseDatePst;
    }

    public void setPurchaseDatePst(String purchaseDatePst) {
        this.purchaseDatePst = purchaseDatePst;
    }

    public String getOriginalPurchaseDate() {
        return originalPurchaseDate;
    }

    public void setOriginalPurchaseDate(String originalPurchaseDate) {
        this.originalPurchaseDate = originalPurchaseDate;
    }

    public String getOriginalPurchaseDateMs() {
        return originalPurchaseDateMs;
    }

    public void setOriginalPurchaseDateMs(String originalPurchaseDateMs) {
        this.originalPurchaseDateMs = originalPurchaseDateMs;
    }

    public String getOriginalPurchaseDatePst() {
        return originalPurchaseDatePst;
    }

    public void setOriginalPurchaseDatePst(String originalPurchaseDatePst) {
        this.originalPurchaseDatePst = originalPurchaseDatePst;
    }

    public String getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(String expiresDate) {
        this.expiresDate = expiresDate;
    }

    public String getExpiresDateMs() {
        return expiresDateMs;
    }

    public void setExpiresDateMs(String expiresDateMs) {
        this.expiresDateMs = expiresDateMs;
    }

    public String getExpiresDatePst() {
        return expiresDatePst;
    }

    public void setExpiresDatePst(String expiresDatePst) {
        this.expiresDatePst = expiresDatePst;
    }

    public String getWebOrderLineItemId() {
        return webOrderLineItemId;
    }

    public void setWebOrderLineItemId(String webOrderLineItemId) {
        this.webOrderLineItemId = webOrderLineItemId;
    }

    public String getIsTrialPeriod() {
        return isTrialPeriod;
    }

    public void setIsTrialPeriod(String isTrialPeriod) {
        this.isTrialPeriod = isTrialPeriod;
    }

    public String getIsInIntroOfferPeriod() {
        return isInIntroOfferPeriod;
    }

    public void setIsInIntroOfferPeriod(String isInIntroOfferPeriod) {
        this.isInIntroOfferPeriod = isInIntroOfferPeriod;
    }

    public String getInAppOwnershipType() {
        return inAppOwnershipType;
    }

    public void setInAppOwnershipType(String inAppOwnershipType) {
        this.inAppOwnershipType = inAppOwnershipType;
    }

    public String getSubscriptionGroupIdentifier() {
        return subscriptionGroupIdentifier;
    }

    public void setSubscriptionGroupIdentifier(String subscriptionGroupIdentifier) {
        this.subscriptionGroupIdentifier = subscriptionGroupIdentifier;
    }

    @Override
    public String toString() {
        return "productId='" + productId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", originalTransactionId='" + originalTransactionId + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", purchaseDateMs='" + purchaseDateMs + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object other) {
        if (!(other instanceof InAppItem)) {
            return -1;
        }
        int result = (int)((((InAppItem) other).getPurchaseDateMs() - purchaseDateMs) / 1000);
        return result;
    }
}