package com.lineying.service;

import com.lineying.sms.SmsEntity;

/**
 * 短信服务
 */
public interface ISmsService {

    /**
     * @param phone  给手机号发送验证码
     * @param code 验证码（腾讯云限制只能是6位以内纯数字）
     * @return
     */
    int sendCode(SmsEntity bean, String phone, String code);

}
