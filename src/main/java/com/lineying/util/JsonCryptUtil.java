package com.lineying.util;

import com.lineying.common.CommonConstant;

/**
 * json加密结果
 */
public class JsonCryptUtil {

    /**
     * 返回时间错误结果
     * @return
     */
    public static String makeFailTime() {

        String result = JsonUtil.makeFailTime();
        System.out.println("时间错误原始结果::" + result);
        return AESUtil.encryptCBC(CommonConstant.DB_SECRET_KEY, result);
    }

    /**
     * 返回简单结果
     * @param flag
     * @return
     */
    public static String makeResult(boolean flag) {
        String result = JsonUtil.makeResult(flag);
        System.out.println("原始结果::" + result);
        return AESUtil.encryptCBC(CommonConstant.DB_SECRET_KEY, result);
    }

    /**
     * 返回数据结果
     * @param obj
     * @return
     */
    public static String makeSuccess(Object obj) {
        String result = JsonUtil.makeSuccess(obj);
        System.out.println("成功原始结果::" + result);
        return AESUtil.encryptCBC(CommonConstant.DB_SECRET_KEY, result);
    }

}
