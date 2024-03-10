package com.lineying.controller.api.verify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.bean.VerifyCode;
import com.lineying.controller.BaseController;
import com.lineying.service.ISmsService;
import com.lineying.util.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 验证码处理公共类
 */
public class BaseVerifyController extends BaseController {

    // 验证码有效时间
    public static final int VERIFY_INTERVAL = 2 * 60;
    public static final int VERIFY_INTERVAL_CLEAR = 24 * 3600;

    public static Map<String, VerifyCode> mVerifyCodes = new HashMap<>();

    @Resource
    protected ISmsService smsService;

    /**
     * 执行验证码缓存清除
     */
    protected void clearVerifyCodes() {
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
    protected VerifyCode getCacheVerifyCode(String targetKey) {
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
    protected String makeTargetKey(String appCode, int type, String target) {
        return String.format("%s_%s_%s", appCode, type + "", target);
    }

    /**
     * 返回成功结果，包含过期时间
     * @param timestamp 验证码生成时间
     * @return
     */
    protected String makeSuccess(long timestamp) {
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

}