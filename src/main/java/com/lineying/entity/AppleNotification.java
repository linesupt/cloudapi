package com.lineying.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 苹果通知
 */
public class AppleNotification {

    public AppleNotification() { }

    ////////////////////-通知基础数据-//////////////////////
    @Expose
    @SerializedName("notificationUUID")
    String notificationUUID;

    @Expose
    @SerializedName("notificationType")
    String notificationType;

    @Expose
    @SerializedName("signedDate")
    long signedDate;

    @Expose
    @SerializedName("version")
    String version;

    /////////////////////-通知数据（从data数据中解析）-/////////////////////
    @Expose
    @SerializedName("signedTransactionInfo")
    String signedTransactionInfo;

    @Expose
    @SerializedName("environment")
    String environment;

    @Expose
    @SerializedName("bundleVersion")
    String bundleVersion;

    @Expose
    @SerializedName("bundleId")
    String bundleId;

    @Expose
    @SerializedName("appAppleId")
    long appAppleId;

    @Expose
    @SerializedName("signedRenewalInfo")
    String signedRenewalInfo;

    @Expose
    @SerializedName("status")
    int status;

    ///////////////////来自signedTransactionInfo字段解析///////////////////
    @Expose
    @SerializedName("transactionId")
    String transactionId;

    @Expose
    @SerializedName("originalTransactionId")
    String originalTransactionId;

    @Expose
    @SerializedName("productId")
    String productId;

    public String getNotificationUUID() {
        return notificationUUID;
    }

    public void setNotificationUUID(String notificationUUID) {
        this.notificationUUID = notificationUUID;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public long getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(long signedDate) {
        this.signedDate = signedDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSignedTransactionInfo() {
        return signedTransactionInfo;
    }

    public void setSignedTransactionInfo(String signedTransactionInfo) {
        this.signedTransactionInfo = signedTransactionInfo;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getBundleVersion() {
        return bundleVersion;
    }

    public void setBundleVersion(String bundleVersion) {
        this.bundleVersion = bundleVersion;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public long getAppAppleId() {
        return appAppleId;
    }

    public void setAppAppleId(long appAppleId) {
        this.appAppleId = appAppleId;
    }

    public String getSignedRenewalInfo() {
        return signedRenewalInfo;
    }

    public void setSignedRenewalInfo(String signedRenewalInfo) {
        this.signedRenewalInfo = signedRenewalInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "AppleNotification{" +
                "notificationUUID='" + notificationUUID + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", signedDate=" + signedDate +
                ", version='" + version + '\'' +
                ", signedTransactionInfo='" + signedTransactionInfo + '\'' +
                ", environment='" + environment + '\'' +
                ", bundleVersion='" + bundleVersion + '\'' +
                ", bundleId='" + bundleId + '\'' +
                ", appAppleId=" + appAppleId +
                ", signedRenewalInfo='" + signedRenewalInfo + '\'' +
                ", status=" + status +
                ", transactionId='" + transactionId + '\'' +
                ", originalTransactionId='" + originalTransactionId + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }
}
