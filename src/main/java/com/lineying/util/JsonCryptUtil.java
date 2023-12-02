package com.lineying.util;

import com.lineying.common.CommonConstant;

/**
 * json加密结果
 */
public class JsonCryptUtil {

    /**
     * 公钥不匹配
     * @return
     */
    public static String makeFailKey() {

        String result = JsonUtil.makeFailKey();
        System.out.println("公钥错误::" + result);
        return AESUtil.encrypt(CommonConstant.DB_SECRET_KEY, CommonConstant.IV_SEED, result);
    }

    /**
     * 签名验证错误
     * @return
     */
    public static String makeFailSign() {

        String result = JsonUtil.makeFailSign();
        System.out.println("签名验证错误::" + result);
        return AESUtil.encrypt(CommonConstant.DB_SECRET_KEY, CommonConstant.IV_SEED, result);
    }

    /**
     * 返回时间错误结果
     * @return
     */
    public static String makeFailTime() {

        String result = JsonUtil.makeFailTime();
        System.out.println("时间错误::" + result);
        return AESUtil.encrypt(CommonConstant.DB_SECRET_KEY, CommonConstant.IV_SEED, result);
    }

    /**
     * 返回简单结果
     * @param flag
     * @return
     */
    public static String makeResult(boolean flag) {
        String result = JsonUtil.makeResult(flag);
        System.out.println("结果::" + result);
        return AESUtil.encrypt(CommonConstant.DB_SECRET_KEY, CommonConstant.IV_SEED, result);
    }

    /**
     * 返回数据结果
     * @param obj
     * @return
     */
    public static String makeSuccess(Object obj) {
        String result = JsonUtil.makeSuccess(obj);
        System.out.println("成功::" + result);
        return AESUtil.encrypt(CommonConstant.DB_SECRET_KEY, CommonConstant.IV_SEED, result);
    }

}
