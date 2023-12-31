package com.lineying.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 验证码生成器
 */
public class VerifyCodeGenerator {

    // 字母&数字
    // private static final String LIMIT_CODE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // 移除不易分辨的0、1、i、l、O
    private static final String LIMIT_CODE = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
    // 纯数字
    private static final String LIMIT_CODE_NUM = "0123456789";

    /**
     * 随机生成验证码(字母+数字)
     * @return
     */
    public static String generate() {
        return getCode(6, false);
    }

    /**
     * 生成数字随机码
     * @return
     */
    public static String generateNum() {
        return getCode(6, true);
    }

    public static String getCode(int len, boolean isNum){
        String codes = isNum ? LIMIT_CODE_NUM : LIMIT_CODE;
        //验证码生成范围
        char[] c = codes.toCharArray();
        StringBuffer str = new StringBuffer("");
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < len; i++) {
            char code = c[current.nextInt(0, codes.length())];
            str.append(code);
        }
        return str.toString();
    }

}
