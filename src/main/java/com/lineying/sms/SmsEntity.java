package com.lineying.sms;

/**
 * 短信APP实体
 */
public class SmsEntity {

    // 服务器内部定义应用代码，用于区分应用
    private String appcode;
    // 腾讯云短信应用ID
    private String appId;
    // 短信签名内容
    private String signName;
    // 短信模板
    private String templateId;

    public SmsEntity(String appcode, String appId, String signName, String templateId) {
        this.appcode = appcode;
        this.appId = appId;
        this.signName = signName;
        this.templateId = templateId;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

}
