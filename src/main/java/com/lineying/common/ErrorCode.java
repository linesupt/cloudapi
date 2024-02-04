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

}
