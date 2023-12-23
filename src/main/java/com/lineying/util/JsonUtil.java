package com.lineying.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * json对象
 *
 * @author ganjing
 */
public class JsonUtil {

    /**
     * 验证码错误
     * @return
     */
    public static String makeFailVerifyCode() {
        return makeResult(0, "fail, err: verify code error", null);
    }


    /**
     * 验证码错误
     * @return
     */
    public static String makeFailVerifyTimeout() {
        return makeResult(0, "fail, err: verify code timeout", null);
    }

    /**
     * 发送验证码错误
     * @return
     */
    public static String makeFailSendVerifyCode() {
        return makeResult(0, "fail, err: send verify code error", null);
    }

    /**
     * 验证码成功
     * @return
     */
    public static String makeSuccess() {
        return makeResult(true);
    }

    /**
     * 公钥不匹配
     * @return
     */
    public static String makeFailKey() {
        return makeResult(0, "fail, err: key error", null);
    }

    /**
     * 签名验证错误
     * @return
     */
    public static String makeFailSign() {
        return makeResult(0, "fail, err: sign error", null);
    }

    /**
     * 时间错误
     * @return
     */
    public static String makeFailTime() {
        return makeResult(0, "fail, err: timestamp error", null);
    }

    /**
     * 数据为空
     * @return
     */
    public static String makeFailNoData() {
        return makeResult(0, "fail, err: data null", null);
    }

    /**
     * 数据过期
     * @return
     */
    public static String makeFailDataExpired() {
        return makeResult(0, "fail, err: data expired", null);
    }

    /**
     * 失败
     * @param cause
     * @return
     */
    public static String makeFail(String cause) {
        return makeResult(0, "fail, err: " + cause, null);
    }

    /**
     * 生成结果
     * @param result true：成功 false：失败
     * @return
     */
    public static String makeResult(boolean result) {
        int code = result ? 1 : 0;
        String msg = result ? "success" : "fail";
        return makeResult(code, msg, null);
    }

    /**
     * 生成结果
     * @param code 0：失败 1：成功
     * @param msg 备注： success、fail, err:+原因
     * @param data
     * @return
     */
    public static String makeResult(int code, String msg, String data) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("code", code);
        jsonMap.put("msg", msg);
        jsonMap.put("data", data);
        return JSON.toJSONString(jsonMap, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 返回结果
     * @param data 返回实际数据
     * @return
     */
    public static String makeSuccess(Object data) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("code", 1);
        jsonMap.put("msg", "success");
        jsonMap.put("data", data);
        return JSON.toJSONString(jsonMap, SerializerFeature.WriteMapNullValue);
    }

}
