package com.lineying.controller.sms;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 应用级接口,短信验证
 */
@RestController
@RequestMapping("api/sms")
public class SmsController {

    /**
     * 发送短信验证码
     * @return
     */
    @RequestMapping("/send_code")
    public String sendCode(HttpServletRequest request) {

        return "";
    }

    /**
     * 短信验证
     * @return
     */
    @RequestMapping("/code_verify")
    public String codeVerify(HttpServletRequest request) {

        return "";
    }

}
