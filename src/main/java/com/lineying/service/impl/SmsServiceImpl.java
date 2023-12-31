package com.lineying.service.impl;

import com.lineying.service.ISmsService;
import com.lineying.sms.SmsEntity;
import com.lineying.sms.SmsSender;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements ISmsService {

    private SmsSender makeSender() {
        return new SmsSender();
    }

    @Override
    public int sendCode(SmsEntity bean, String phone, String code) {
        SmsSender sender = makeSender();
        return sender.sendCode(phone, code, bean.getAppId(), bean.getSignName(), bean.getTemplateId());
    }

}
