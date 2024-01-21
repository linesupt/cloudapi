package com.lineying.controller.verify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.bean.VerifyCode;
import com.lineying.common.AppCodeManager;
import com.lineying.common.SignResult;
import com.lineying.controller.BaseController;
import com.lineying.mail.EmailSenderManager;
import com.lineying.service.ISmsService;
import com.lineying.sms.SmsEntity;
import com.lineying.sms.SmsEntityFactory;
import com.lineying.util.*;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

/**
 * 应用级接口,验证码验证
 */
@RestController
@RequestMapping("api/verify")
public class VerifyController extends BaseController {

    // 验证码有效时间
    public static final int VERIFY_INTERVAL = 2 * 60;
    public static final int VERIFY_INTERVAL_CLEAR = 24 * 3600;

    private static Map<String, VerifyCode> mVerifyCodes = new HashMap<>();

    // 简体中文
    private List<String> zhCNs = Arrays.asList("zh-CN", "zh_CN", "zh-Hans");
    // 繁体中文
    private List<String> zhHants = Arrays.asList("zh-TW", "zh_TW", "zh-Hant");

    @Resource
    ISmsService smsService;

    /**
     * 获取语言类环境
     * @param locale
     * @return
     */
    private Locale getLocale(String locale) {
        if (zhCNs.contains(locale)) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (zhHants.contains(locale)) {
            return Locale.TRADITIONAL_CHINESE;
        }
        return Locale.ENGLISH;
    }

    /**
     * 执行验证码缓存清除
     */
    private void clearVerifyCodes() {
        Set<String> keySet = mVerifyCodes.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String targetKey = iterator.next();
            VerifyCode entity = mVerifyCodes.get(targetKey);
            if (getCurrentTime() - entity.getTimestamp() > VERIFY_INTERVAL_CLEAR) {
                mVerifyCodes.remove(targetKey);
            }
        }
    }

    /**
     * 获取符合的验证码对象
     * @param targetKey
     * @return
     */
    private VerifyCode getCacheVerifyCode(String targetKey) {
        VerifyCode entity = mVerifyCodes.get(targetKey);
        if (entity == null) {
            return null;
        }
        return entity;
    }

    /**
     * 生成缓存key
     * @param appCode
     * @param type
     * @param target
     * @return
     */
    private String makeTargetKey(String appCode, int type, String target) {
        return String.format("%s_%s_%s", appCode, type + "", target);
    }

    /**
     * 返回成功结果，包含过期时间
     * @param timestamp 验证码生成时间
     * @return
     */
    private String makeSuccess(long timestamp) {
        // 直接返回、避免用户攻击
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        long expireTime = timestamp + VERIFY_INTERVAL;
        long remainInterval = expireTime - getCurrentTime();
        // 后端时间不准可能引起问题
        map.put("expire_time", expireTime);
        // 返回剩余时间更可靠
        map.put("remain_interval", remainInterval);
        list.add(map);

        JsonObject obj = new JsonObject();
        obj.add("data", new Gson().toJsonTree(list));
        return JsonCryptUtil.makeSuccess(obj);
    }

    /**
     * 是否过期
     * @return
     */
    public boolean isExpired(long timestamp) {
        if (getCurrentTime() - timestamp > VERIFY_INTERVAL) {
            return true;
        }
        return false;
    }

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
            String subject = messageSource.getMessage("email_verify_title", null, getLocale(locale));
            String verifyMsg = messageSource.getMessage("email_verify_msg", null, getLocale(locale));
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