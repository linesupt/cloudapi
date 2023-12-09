package com.lineying.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 签名验证结果
 */
@Retention(RetentionPolicy.SOURCE)
public @interface SignResult {
    int OK = 1;
    int KEY_ERROR = 2; // 公钥不匹配
    int SIGN_ERROR = 3; // 签名错误
}