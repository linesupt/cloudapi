package com.lineying.controller.verify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lineying.bean.VerifyCode;
import com.lineying.common.SignResult;
import com.lineying.controller.BaseController;
import com.lineying.mail.EmailSenderManager;
import com.lineying.service.ISmsService;
import com.lineying.util.AESUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.SignUtil;
import com.lineying.util.VerifyCodeGenerator;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
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

    private List<String> mAppCodeServers = Arrays.asList("mathcalc", "scancode", "linevideo");
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

    // 创建消息资源
    private MessageSource buildMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setFallbackToSystemLocale(true);
        messageSource.setCacheSeconds(-1);
        messageSource.setAlwaysUseMessageFormat(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @RequestMapping("/test_locale")
    public void testLocale() {
        MessageSource messageSource = buildMessageSource();
        System.out.println("======>>> " + messageSource.getMessage("email_verify_title", null, getLocale("zh-CN")));
        System.out.println("======>>> " + messageSource.getMessage("email_verify_msg", null, getLocale("zh-CN")));
        System.out.println("======>>> " + messageSource.getMessage("email_verify_title", null, getLocale("zh-Hans")));
        System.out.println("======>>> " + messageSource.getMessage("email_verify_msg", null, getLocale("zh-Hans")));
        System.out.println("======>>> " + messageSource.getMessage("email_verify_title", null, getLocale("zh-TW")));
        System.out.println("======>>> " + messageSource.getMessage("email_verify_msg", null, getLocale("zh-TW")));
        System.out.println("======>>> " + messageSource.getMessage("email_verify_title", null, getLocale("zh-Hant")));
        System.out.println("======>>> " + messageSource.getMessage("email_verify_msg", null, getLocale("zh-Hant")));
        System.out.println("======>>> " + messageSource.getMessage("email_verify_title", null, getLocale("en")));
        System.out.println("======>>> " + messageSource.getMessage("email_verify_msg", null, getLocale("en")));
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
        JSONObject obj = new JSONObject();
        obj.put("data", JSON.toJSON(list));
        return JsonCryptUtil.makeSuccess(obj);
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
        JSONObject jsonObject = JSON.parseObject(data);
        Logger.getGlobal().info("data::" + data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String appCode = jsonObject.getString("appcode");
        int type = jsonObject.getInteger("type");
        String target = jsonObject.getString("target");
        String targetKey = makeTargetKey(appCode, type, target);

        int sendResult = 0;
        if (!mAppCodeServers.contains(appCode)) {
            Logger.getGlobal().info("不存在当前应用::" + appCode);
            return JsonCryptUtil.makeFailSendVerifyCode();
        }
        String sendCode = "";
        VerifyCode cacheVerifyCode = getCacheVerifyCode(targetKey);
        if (cacheVerifyCode == null || cacheVerifyCode.isExpired()) {
            sendCode = VerifyCodeGenerator.generate();
        } else {
            timestamp = cacheVerifyCode.getTimestamp();
            return makeSuccess(timestamp);
        }

        MessageSource messageSource = buildMessageSource();
        // 生成验证码, 执行邮件发送逻辑
        if (type == 1) {
            // 处理邮件发送
            String subject = messageSource.getMessage("email_verify_title", null, getLocale(locale));
            String verifyMsg = messageSource.getMessage("email_verify_msg", null, getLocale(locale));
            String content = String.format(verifyMsg, sendCode);
            sendResult = EmailSenderManager.relayEmail(subject, content, target);
            if (sendResult == 0) {
                Logger.getGlobal().info("邮件发送失败!");
            }
        } else if (type == 2) {
            sendResult = smsService.sendCode(appCode, target, sendCode);
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
        JSONObject jsonObject = JSON.parseObject(data);
        Logger.getGlobal().info("data::" + data);
        long timestamp = jsonObject.getLong("timestamp");
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        String appCode = jsonObject.getString("appcode");
        String code = jsonObject.getString("code");
        String target = jsonObject.getString("target");
        int type = jsonObject.getInteger("type");
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
            if (entity.isExpired()) {
                // 验证码已经过期
                return JsonCryptUtil.makeFailVerifyTimeout();
            }
        }

        mVerifyCodes.remove(targetKey);
        return JsonCryptUtil.makeSuccess();
    }

}