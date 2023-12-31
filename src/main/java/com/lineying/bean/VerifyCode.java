package com.lineying.bean;

import static com.lineying.controller.verify.VerifyController.VERIFY_INTERVAL_CLEAR;

/**
 * 验证码信息
 */
public class VerifyCode {

    // 对应app
    private String appCode;
    private String target;
    private String code;
    private int type;
    private long timestamp;

    public VerifyCode() {

    }

    public VerifyCode(String appCode, String target, String code, int type, long timestamp) {
        this.appCode = appCode;
        this.target = target;
        this.code = code;
        this.type = type;
        this.timestamp = timestamp;
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

    /**
     * 是否过期
     * @return
     */
    public boolean isExpired() {
        if (System.currentTimeMillis() / 1000 - timestamp > VERIFY_INTERVAL_CLEAR) {
            return true;
        }
        return false;
    }

}
