package com.lineying.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮箱、手机号验证
 */
public class VerifyUtil {

    // 中国大陆
    private static final String PHONE_PATTERN = "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$";
    // 邮件
    private static final String EMAIL_PATTERN = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
