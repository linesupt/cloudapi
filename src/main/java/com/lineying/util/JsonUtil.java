package com.lineying.util;

import com.google.gson.Gson;
import com.lineying.common.ErrorCode;
import com.lineying.data.Param;

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
        return makeResult(ErrorCode.FAIL, "fail, err: verify code error", null);
    }


    /**
     * 验证码错误
     * @return
     */
    public static String makeFailVerifyTimeout() {
        return makeResult(ErrorCode.FAIL, "fail, err: verify code timeout", null);
    }

    /**
     * 发送验证码错误
     * @return
     */
    public static String makeFailSendVerifyCode() {
        return makeResult(ErrorCode.FAIL, "fail, err: send verify code error", null);
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
        return makeResult(ErrorCode.FAIL, "fail, err: key error", null);
    }

    /**
     * 签名验证错误
     * @return
     */
    public static String makeFailSign() {
        return makeResult(ErrorCode.FAIL, "fail, err: sign error", null);
    }

    /**
     * 时间错误
     * @return
     */
    public static String makeFailTime() {
        return makeResult(ErrorCode.FAIL, "fail, err: timestamp error", null);
    }

    /**
     * 数据为空
     * @return
     */
    public static String makeFailNoData() {
        return makeResult(ErrorCode.FAIL, "fail, err: data null", null);
    }

    /**
     * 数据过期
     * @return
     */
    public static String makeFailDataExpired() {
        return makeResult(ErrorCode.FAIL, "fail, err: data expired", null);
    }

    /**
     * 失败
     * @param cause
     * @return
     */
    public static String makeFail(String cause) {
        return makeResult(ErrorCode.FAIL, "fail, err: " + cause, null);
    }

    /**
     * 失败
     * @param cause
     * @param statusCode
     * @return
     */
    public static String makeFailError(String cause, int statusCode) {
        return makeResultError(ErrorCode.FAIL, "fail, err: " + cause, null, statusCode);
    }

    /**
     * 生成结果
     * @param result true：成功 false：失败
     * @return
     */
    public static String makeResult(boolean result) {
        int code = result ? ErrorCode.SUCCESS : ErrorCode.FAIL;
        String msg = result ? Param.Result.SUCCESS : Param.Result.FAIL;
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
        jsonMap.put(Param.Key.CODE, code);
        jsonMap.put(Param.Key.MSG, msg);
        jsonMap.put(Param.Key.DATA, data);
        return new Gson().toJson(jsonMap);
    }

    /**
     * 返回包含错误码的数据
     * @param code
     * @param msg
     * @param data
     * @param statusCode
     * @return
     */
    public static String makeResultError(int code, String msg, String data, int statusCode) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(Param.Key.CODE, code);
        jsonMap.put(Param.Key.MSG, msg);
        jsonMap.put(Param.Key.STATUS, statusCode);
        jsonMap.put(Param.Key.DATA, data);
        return new Gson().toJson(jsonMap);
    }

    /**
     * 返回结果
     * @param data 返回实际数据
     * @return
     */
    public static String makeSuccess(Object data) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(Param.Key.CODE, ErrorCode.SUCCESS);
        jsonMap.put(Param.Key.MSG, Param.Result.SUCCESS);
        jsonMap.put(Param.Key.DATA, data);
        return new Gson().toJson(jsonMap);
    }

    /**
     * 处理微信支付返回结果
     * @param isSuccess
     * @return
     */
    public static String makeWXPayResult(boolean isSuccess) {
        Map<String, String> jsonMap = new HashMap<>();
        if (isSuccess) {
            jsonMap.put(Param.Key.CODE, Param.Result.CAP_SUCCESS);
            jsonMap.put(Param.Key.MESSAGE, Param.Result.CAP_SUCCESS);
        } else {
            jsonMap.put(Param.Key.CODE, Param.Result.CAP_FAIL);
            jsonMap.put(Param.Key.MESSAGE, Param.Result.CAP_FAIL);
        }
        return new Gson().toJson(jsonMap);
    }

    /**
     * 生成json
     * @param jsonMap
     * @return
     */
    public static String makeData(Map<String, Object> jsonMap) {
        return new Gson().toJson(jsonMap);
    }

}
