package com.lineying.controller.api.pay;

/**
 * IOS通知请求体
 */
public class IosNotificationRequest {

    private String signedPayLoad;

    public String getSignedPayLoad() {
        return signedPayLoad;
    }

    public void setSignedPayLoad(String signedPayLoad) {
        this.signedPayLoad = signedPayLoad;
    }

}
