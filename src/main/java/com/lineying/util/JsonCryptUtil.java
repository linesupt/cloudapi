package com.lineying.util;

import com.lineying.common.CommonConstant;

/**
 * json加密结果
 */
public class JsonCryptUtil {

    /**
     * 验证码验证失败
     * @return
     */
    public static String makeFailVerifyCode() {

        String result = JsonUtil.makeFailVerifyCode();
        System.out.println("验证码错误::" + result);
        return AESUtil.encrypt(CommonConstant.DB_SECRET_KEY, CommonConstant.IV_SEED, result);
    }

    /**
     * 验证超时
     * @return
     */
    public static String makeFailVerifyTimeout() {

        String result = JsonUtil.makeFailVerifyTimeout();
        System.out.println("验证码错误::" + result);
        return AESUtil.encrypt(CommonConstant.DB_SECRET_KEY, CommonConstant.IV_SEED, result);
    }

    /**
     * 发送验证码失败
     * @return
     */
    public static String makeFailSendVerifyCode() {

        String result = JsonUtil.makeFailSendVerifyCode();
        System.out.println("发送验证码错误::" + result);
        return AESUtil.encrypt(CommonConstant.DB_SECRET_KEY, CommonConstant.IV_SEED, result);
    }

    /**
     * 返回成功
     * @return
     */
    public static String makeSuccess() {
        String result = JsonUtil.makeSuccess();
        System.out.println("成功::" + result);
        return AESUtil.encrypt(CommonConstant.DB_SECRET_KEY, CommonConstant.IV_SEED, result);
    }

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
     * 生成错误原因
     * @param cause
     * @return
     */
    public static String makeFail(String cause) {
        String result = JsonUtil.makeFail(cause);
        System.out.println("原因错误::" + result);
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
