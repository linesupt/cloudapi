package com.lineying.common;

import java.lang.annotation.*;

/**
 * 登录方式
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD,
        ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface LoginType {

    // 用户密码登录（默认）
    int USERNAME = 0;
    // 邮箱密码登录
    int EMAIL = 1;
    // token登录
    int TOKEN = 2;
    // Apple登录
    int APPLE = 3;
    // 微信登录
    int WECHAT = 4;

}
