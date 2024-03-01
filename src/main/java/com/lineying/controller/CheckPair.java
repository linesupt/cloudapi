package com.lineying.controller;

import com.google.gson.JsonObject;

/**
 * 验证结果
 */
public class CheckPair {

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

    public CheckPair(JsonObject dataObject, String result) {
        this(dataObject, result, 0);
    }

    public CheckPair(JsonObject dataObject, String result, long timestamp) {
        this.dataObject = dataObject;
        this.result = result;
        this.timestamp = timestamp;
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
