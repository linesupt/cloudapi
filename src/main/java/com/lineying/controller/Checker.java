package com.lineying.controller;

import com.google.gson.JsonObject;

/**
 * 验证结果
 */
public class Checker {

    /**
     * 平台类型
     */
    private String platform;

    /**
     * 语言环境
     */
    private String locale;

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 应用code
     */
    private String appcode;

    /**
     * 应用版本
     */
    private String appVersion;

    /**
     * 设备品牌
     */
    private String brand;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 请求数据部分
     */
    private JsonObject dataObject;

    /**
     * 验证结果
     */
    private String result;

    /**
     * 客户端时间戳
     */
    private long timestamp;

    public Checker(String platform, String locale, String token, String appcode, String brand, String model, String appVersion, JsonObject dataObject, String result) {
        this(platform, locale, token, appcode, brand, model, appVersion, dataObject, result, 0);
    }

    public Checker(String platform, String locale, String token, String appcode, String brand, String model, String appVersion, JsonObject dataObject, String result, long timestamp) {
        this.platform = platform;
        this.locale = locale;
        this.token = token;
        this.appcode = appcode;
        this.brand = brand;
        this.model = model;
        this.appVersion = appVersion;
        this.dataObject = dataObject;
        this.result = result;
        this.timestamp = timestamp;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public JsonObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(JsonObject dataObject) {
        this.dataObject = dataObject;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 是否验证通过
     * @return
     */
    public boolean isValid() {
        return dataObject != null && result == null;
    }

}
