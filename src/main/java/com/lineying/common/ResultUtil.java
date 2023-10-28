package com.lineying.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果map
 *
 * @author ganjing
 */
public class ResultUtil {

    public static final int SUCCESS_CODE = 10000;
    public static final int ERROR_CODE = 50000;
    public static final String STATUS_NAME = "status";
    public static final String MSG = "msg";
    public static final String SUCCESS_MSG = "ok";
    public static final String SIGN_ERROR_MSG = "验证签名不正确，你没有权限调用接口";

    //成功方法
    public static Map<String, Object> success() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(ResultUtil.STATUS_NAME, ResultUtil.SUCCESS_CODE);
        resultMap.put(ResultUtil.MSG, ResultUtil.SUCCESS_MSG);
        return resultMap;
    }

    //成功方法
    public static Map<String, Object> success(Object obj) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(ResultUtil.STATUS_NAME, ResultUtil.SUCCESS_CODE);
        resultMap.put(ResultUtil.MSG, ResultUtil.SUCCESS_MSG);
        resultMap.put("result", obj);
        return resultMap;
    }

    //失败方法
    public static Map<String, Object> error(String msg) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(ResultUtil.STATUS_NAME, ResultUtil.ERROR_CODE);
        resultMap.put(ResultUtil.MSG, msg);
        return resultMap;
    }

    //失败方法
    public static Map<String, Object> signError() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(ResultUtil.STATUS_NAME, ResultUtil.ERROR_CODE);
        resultMap.put(ResultUtil.MSG, SIGN_ERROR_MSG);
        return resultMap;
    }
}
