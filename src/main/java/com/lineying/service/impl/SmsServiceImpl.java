package com.lineying.service.impl;

import com.lineying.service.ISmsService;
import com.lineying.sms.MathCalcSmsComponent;
import com.lineying.sms.ScanCodeSmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SmsServiceImpl implements ISmsService {

    @Autowired
    MathCalcSmsComponent mathCalcSmsComponent;
    @Autowired
    ScanCodeSmsComponent scanCodeSmsComponent;

    @Override
    public int sendCode(String appcode, String phone, String code) {
        int result = 0;
        if (Objects.equals("mathcalc", appcode)) {
            result = mathCalcSmsComponent.sendCode(phone, code);
        } else if (Objects.equals("scancode", appcode)) {
            result = scanCodeSmsComponent.sendCode(phone, code);
        }
        return result;
    }

    @Override
    public boolean codeVerify(String appcode, String phone, String code) {
        return false;
    }


}
