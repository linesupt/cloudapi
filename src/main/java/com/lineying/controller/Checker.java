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

    public Checker(String platform, String locale, JsonObject dataObject, String result) {
        this(platform, locale, dataObject, result, 0);
    }

    public Checker(String platform, String locale, JsonObject dataObject, String result, long timestamp) {
        this.platform = platform;
        this.locale = locale;
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
