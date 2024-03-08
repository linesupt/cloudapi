package com.lineying.common;

/**
 * 状态码、默认不添加到返回值中
 */
public @interface ErrorCode {

    // 邮箱未注册
    int EMAIL_NOT_REGISTER = 10001;
    // 手机号未注册
    int PHONE_NOT_REGISTER = 10002;
    // 修改密码失败
    int MOD_PASSWORD_FAILED = 10003;
    // 注册用户名已存在
    int REGISTER_USERNAME_EXIST = 10004;
    // 注册appleuser已存在
    int REGISTER_APPLE_USER_EXIST = 10005;

}
