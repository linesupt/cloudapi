package com.lineying.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 验证码生成器
 */
public class VerifyCodeGenerator {

    private static final String LIMIT_CODE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 随机生成验证码
     * @return
     */
    public static String generate() {
        return getCode(6);
    }

    public static String getCode(int len){
        //验证码生成范围
        char[] c = LIMIT_CODE.toCharArray();
        StringBuffer str = new StringBuffer("");
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < len; i++) {
            char code = c[current.nextInt(0, LIMIT_CODE.length())];
            str.append(code);
        }
        return str.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            String code = generate();
            System.out.println("code::" + code);
        }
    }

}
