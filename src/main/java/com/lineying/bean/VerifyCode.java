package com.lineying.bean;

/**
 * 验证码信息
 */
public class VerifyCode {

    private int uid;
    // 对应app
    private String appCode;
    private String target;
    private String code;
    private int type;
    private long timestamp;

    public VerifyCode() {

    }

    public VerifyCode(int uid, String appCode, String target, String code, int type, long timestamp) {
        this.uid = uid;
        this.appCode = appCode;
        this.target = target;
        this.code = code;
        this.type = type;
        this.timestamp = timestamp;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
