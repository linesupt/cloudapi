package com.lineying.common;

/**
 * 代表客户端信息
 */
public enum AppEntity {

    // 计算器
    MATHCALC("mathcalc", "_cal", "com.lineying.UnitConverter", 1234239300L),
    // 二维码
    SCANCODE("scancode", "_qrcode", "com.lineying.QRCode", 1464322073L),
    // 视频编辑
    LINEVIDEO("linevideo", "_video", "com.lineying.LineVideo", 1254858737L),
    // 消息信使
    SCANMESSENGER("scanmessenger", "_messenger", "com.lineying.ScanMessenger", 0);

    // 应用标识
    String appcode;
    // 数据表后缀
    String tableSuffix;
    // 苹果包ID
    String bundleId;
    // 苹果应用ID
    long appleId;

    AppEntity(String appcode, String tableSuffix, String bundleId, long appleId) {
        this.appcode = appcode;
        this.tableSuffix = tableSuffix;
        this.bundleId = bundleId;
        this.appleId = appleId;
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

    @Override
    public String toString() {
        return "AppEntity{" +
                "appcode='" + appcode + '\'' +
                ", tableSuffix='" + tableSuffix + '\'' +
                ", bundleId='" + bundleId + '\'' +
                ", appleId='" + appleId + '\'' +
                '}';
    }
}
