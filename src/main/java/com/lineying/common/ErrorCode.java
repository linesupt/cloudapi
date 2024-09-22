package com.lineying.common;

/**
 * 状态码、默认不添加到返回值中
 */
public @interface ErrorCode {

    // 失败
    int FAIL = 0;
    // 成功
    int SUCCESS = 1;
    // 邮箱未注册
    int EMAIL_NOT_REGISTER = 10001;
    // 手机号未注册
    int PHONE_NOT_REGISTER = 10002;
    // 修改密码失败
    int MOD_PASSWORD_FAILED = 10003;
    // 用户名已存在
    int USERNAME_EXIST = 10004;
    // appleuser已存在
    int APPLE_USER_EXIST = 10005;
    // 邮箱已存在
    int EMAIL_EXIST = 10006;
    // 手机号已存在
    int MOBILE_EXIST = 10007;
    // 收据验证失败（验证为空）
    int RECEIPT_VERIFY_EMPTY = 10008;

}
