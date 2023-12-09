package com.lineying.service;

public interface ISmsService {


    /**
     * @param phone  给手机号发送验证码
     * @param code 验证码
     * @return
     */
    int sendCode(String appcode, String phone, String code);

    /**
     * 校验验证码
     */
    boolean codeVerify(String appcode, String phone, String code);

}
