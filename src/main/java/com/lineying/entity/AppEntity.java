package com.lineying.entity;

/**
 * 代表客户端信息
 */
public enum AppEntity {

    // 计算器
    MATHCALC("mathcalc", "_cal", "com.lineying.UnitConverter", 1234239300L, "2280d5323503451791a7faabebfffab6"),
    // 二维码
    SCANCODE("scancode", "_qrcode", "com.lineying.QRCode", 1464322073L, "2b7a18ace6cd4042b6aad66c9f8f7050"),
    // 视频编辑
    LINEVIDEO("linevideo", "_video", "com.lineying.LineVideo", 1254858737L, "cb403f049c0f49d1941ad42a589cbc63"),
    // 消息信使
    SCANMESSENGER("scanmessenger", "_messenger", "com.lineying.ScanMessenger", 0, "");

    // 应用标识
    String appcode;
    // 数据表后缀
    String tableSuffix;
    // 苹果包ID
    String bundleId;
    // 苹果应用ID
    long appleId;
    // 应用共享密钥
    String secret;

    AppEntity(String appcode, String tableSuffix, String bundleId, long appleId, String secret) {
        this.appcode = appcode;
        this.tableSuffix = tableSuffix;
        this.bundleId = bundleId;
        this.appleId = appleId;
        this.secret = secret;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getTableSuffix() {
        return tableSuffix;
    }

    public void setTableSuffix(String tableSuffix) {
        this.tableSuffix = tableSuffix;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public long getAppleId() {
        return appleId;
    }

    public void setAppleId(long appleId) {
        this.appleId = appleId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "AppEntity{" +
                "appcode='" + appcode + '\'' +
                ", tableSuffix='" + tableSuffix + '\'' +
                ", bundleId='" + bundleId + '\'' +
                ", appleId='" + appleId + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
