package com.lineying.controller.v2.pay;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.bean.Order;
import com.lineying.common.*;
import com.lineying.controller.Checker;
import com.lineying.controller.api.pay.PayController;
import com.lineying.entity.CommonSqlManager;
import com.lineying.util.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用级接口
 * 订单生成在同一张表里
 */
@Component
@RestController
@RequestMapping("v2")
public class PayControllerV2 extends PayController {

    /**
     * 创建支付宝支付信息
     *
     * @return
     */
    @RequestMapping("/pay/alipay/mkpay")
    public String alipayAppPay(HttpServletRequest request) {
        return super.alipayAppPay(request);
    }

    /**
     * 创建微信支付信息（APP）
     * @return 返回客户端唤醒微信支付的方法
     */
    @RequestMapping("/pay/wxpay/mkpay")
    public String wxpayAppPay(HttpServletRequest request) {
        return super.wxpayAppPay(request);
    }

    /**
     * 创建订单(Apple)
     * Android通过mkpay自动创建订单
     * @return
     */
    @RequestMapping("/order/add")
    public String createOrder(HttpServletRequest request) {
        Order order = makeOrder(request);
        if (order == null) {
            return JsonCryptUtil.makeFail("create order fail");
        }
        return JsonCryptUtil.makeSuccess();
    }

    /**
     * 验证并消耗验证码
     * 查询验证码时长，并填充用户会员时长
     */
    @RequestMapping("/pay/redeem")
    public String payRedeem(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String appcode = jsonObject.get("appcode").getAsString();
        int uid = jsonObject.get("uid").getAsInt();
        String code = jsonObject.get("code").getAsString();
        String table = AppCodeManager.getRedeemTable(appcode);

        List<Map<String, Object>> list = null;
        try {
            list = commonService.list(CommonSqlManager.queryRedeem(table, appcode, code));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return JsonCryptUtil.makeFail("invalid redeem code");
        }

        Map<String, Object> redeem = list.get(0);
        Long duration = (Long) redeem.get("amount");

        long expireTime = handleExpireTime(appcode, uid, duration);
        if (expireTime <= 0) {
            return JsonCryptUtil.makeFail("redeem fail");
        } else {
            boolean result = commonService.update(CommonSqlManager.consumeRedeem(table, appcode, code));
            if (!result) {
                return JsonCryptUtil.makeFail("redeem fail");
            }
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("expire_time", expireTime);
        resultList.add(map);
        JsonObject resultObj = new JsonObject();
        resultObj.add("data", new Gson().toJsonTree(resultList));
        return JsonCryptUtil.makeSuccess(resultObj);
    }

    /**
     * chu
     * @param appcode
     * @param uid
     * @param durationAdd 待追加的时长
     * @return
     */
    private long handleExpireTime(String appcode, int uid, long durationAdd) {

        // 添加时长
        String table = AppCodeManager.getUserTable(appcode);
        try {
            List<Map<String, Object>> list = commonService.list(CommonSqlManager.queryUser(table, uid));
            if (list != null && !list.isEmpty()) {
                Map<String, Object> objectMap = list.get(0);
                long curExpireTime = (Long) objectMap.get("expire_time");
                long expireTime = getCurrentTimeMs();
                if (curExpireTime < getCurrentTimeMs()) {
                    expireTime = getCurrentTimeMs() + durationAdd;
                } else if (curExpireTime >= getCurrentTimeMs()) {
                    expireTime = curExpireTime + durationAdd;
                }
                boolean result = commonService.update(CommonSqlManager.updateExpireTime(table, uid, expireTime));
                if (result) {
                    return expireTime;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
