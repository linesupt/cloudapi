package com.lineying.controller.api.verify;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.bean.VerifyCode;
import com.lineying.common.AppCodeManager;
import com.lineying.common.LocaleManager;
import com.lineying.common.SignResult;
import com.lineying.mail.EmailSenderManager;
import com.lineying.sms.SmsEntity;
import com.lineying.sms.SmsEntityFactory;
import com.lineying.util.*;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

/**
 * 应用级接口,验证码验证
 */
@RestController
@RequestMapping("api/verify")
public class VerifyController extends BaseVerifyController {

    /**
     * 发送短信验证码
     * @return
     */
    @RequestMapping("/send_code")
    public String sendCode(HttpServletRequest request) {
        String platform = request.getHeader("platform");
        String locale = request.getHeader("locale");
        Logger.getGlobal().info("platform:" + platform + " locale:" + locale);
        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        Logger.getGlobal().info("接收消息::" + key + " - " + secretData + " - " + signature);
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case SignResult.KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SignResult.SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        Logger.getGlobal().info("data::" + data);
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String appCode = jsonObject.get("appcode").getAsString();
        int type = jsonObject.get("type").getAsInt();
        String target = jsonObject.get("target").getAsString();
        String targetKey = makeTargetKey(appCode, type, target);

        int sendResult = 0;
        if (!AppCodeManager.contains(appCode)) {
            Logger.getGlobal().info("不存在当前应用::" + appCode);
            return JsonCryptUtil.makeFailSendVerifyCode();
        }
        VerifyCode cacheVerifyCode = getCacheVerifyCode(targetKey);
        if (cacheVerifyCode != null && !isExpired(cacheVerifyCode.getTimestamp())) {
            timestamp = cacheVerifyCode.getTimestamp();
            return makeSuccess(timestamp);
        }
        String sendCode = "";
        if (type == 1) {
            // 处理邮件发送
            MessageSource messageSource = MessageSourceFactory.buildDefaultMessageSource();
            String subject = messageSource.getMessage("email_verify_title", null, LocaleManager.getLocale(locale));
            String verifyMsg = messageSource.getMessage("email_verify_msg", null, LocaleManager.getLocale(locale));
            sendCode = VerifyCodeGenerator.generate();
            String content = String.format(verifyMsg, sendCode);
            sendResult = EmailSenderManager.relayEmail(subject, content, target);
            if (sendResult == 0) {
                Logger.getGlobal().info("邮件发送失败!");
            }
        } else if (type == 2) {
            String targetPhone = target;
            if (target.startsWith("+")) {
                if (!target.startsWith("+86")) {
                    return JsonCryptUtil.makeFail("phone not supported");
                }
                targetPhone = targetPhone.replace("+86", "");
            }
            if (!VerifyUtil.isPhone(targetPhone)) {
                return JsonCryptUtil.makeFail("phone not supported");
            }
            SmsEntity smsEntity = SmsEntityFactory.make(appCode);
            if (smsEntity == null) {
                Logger.getGlobal().info("appcode " + appCode + " not supported");
                return JsonCryptUtil.makeFail("appcode not supported");
            }
            sendCode = VerifyCodeGenerator.generateNum();
            sendResult = smsService.sendCode(smsEntity, target, sendCode);
            if (sendResult == 0) {
                Logger.getGlobal().info("短信发送失败!");
            }
        }

        if (sendResult == 0) {
            return JsonCryptUtil.makeFailSendVerifyCode();
        }

        clearVerifyCodes(); // 执行清理
        Logger.getGlobal().info("生成验证码::" + sendCode);
        VerifyCode entity = new VerifyCode(appCode, target, sendCode, type, timestamp);
        mVerifyCodes.put(targetKey, entity);
        return makeSuccess(timestamp);
    }

    /**
     * 短信验证
     * @return
     */
    @RequestMapping("/code_verify")
    public String codeVerify(HttpServletRequest request) {
        String platform = request.getHeader("platform");
        String locale = request.getHeader("locale");
        Logger.getGlobal().info("platform:" + platform + " locale:" + locale);
        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case SignResult.KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SignResult.SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        Logger.getGlobal().info("data::" + data);
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String appCode = jsonObject.get("appcode").getAsString();
        String code = jsonObject.get("code").getAsString();
        String target = jsonObject.get("target").getAsString();
        int type = jsonObject.get("type").getAsInt();
        String targetKey = makeTargetKey(appCode, type, target);

        VerifyCode entity = getCacheVerifyCode(targetKey);
        if (entity == null) {
            return JsonCryptUtil.makeFailVerifyCode();
        } else {
            if (!Objects.equals(appCode, entity.getAppCode())) {
                return JsonCryptUtil.makeFailVerifyCode();
            }
            if (type != entity.getType()) {
                return JsonCryptUtil.makeFailVerifyCode();
            }
            // 对比验证码
            String serverCode = entity.getCode().toLowerCase();
            String targetCode = code.toLowerCase();
            if (!Objects.equals(serverCode, targetCode)) {
                return JsonCryptUtil.makeFailVerifyCode();
            }
            // 对比时间
            if (isExpired(entity.getTimestamp())) {
                // 验证码已经过期
                return JsonCryptUtil.makeFailVerifyTimeout();
            }
        }

        mVerifyCodes.remove(targetKey);
        return JsonCryptUtil.makeSuccess();
    }

}