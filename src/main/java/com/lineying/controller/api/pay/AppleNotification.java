package com.lineying.controller.api.pay;

/**
 * Apple通知请求体
 */
public class AppleNotification {

    private String signedPayLoad;

    public String getSignedPayLoad() {
        return signedPayLoad;
    }

    public void setSignedPayLoad(String signedPayLoad) {
        this.signedPayLoad = signedPayLoad;
    }

}
