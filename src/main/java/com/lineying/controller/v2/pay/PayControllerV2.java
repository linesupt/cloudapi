package com.lineying.controller.v2.pay;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.bean.Order;
import com.lineying.common.*;
import com.lineying.controller.Checker;
import com.lineying.controller.api.pay.PayController;
import com.lineying.data.Column;
import com.lineying.entity.CommonSqlManager;
import com.lineying.util.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty(Column.OUT_TRADE_NO, order.getOutTradeNo());
        return JsonCryptUtil.makeSuccess(resultObj);
    }

    /**
     * 更新订单状态(Apple)
     * Android通过mkpay自动创建订单
     * @return
     */
    @RequestMapping("/order/update")
    public String updateOrder(HttpServletRequest request) {
        Checker pair = doCheck(request);
        String cause = "order update fail";

        if (!pair.isValid()) {
            return JsonCryptUtil.makeFail(cause);
        }
        try {
            JsonObject jsonObject = pair.getDataObject();
            String appcode = jsonObject.get(Column.APPCODE).getAsString();
            int uid = jsonObject.get(Column.UID).getAsInt();
            String outTradeNo = jsonObject.get(Column.OUT_TRADE_NO).getAsString();
            // 是否有未完成了订单
            boolean hasOrderUpdate = queryOrderStatus(outTradeNo);
            if (!hasOrderUpdate) {
                return JsonCryptUtil.makeFail(cause);
            }

            String goodsCode = queryGoodsCode(outTradeNo);
            if (goodsCode == null || goodsCode.isEmpty()) {
                return JsonCryptUtil.makeFail(cause);
            }

            long duration = queryGoodsDuration(appcode, goodsCode);
            if (duration == 0) {
                return JsonCryptUtil.makeFail(cause);
            }

            long expireTime = handleExpireTime(appcode, uid, duration);
            if (expireTime <= 0) {
                return JsonCryptUtil.makeFail(cause);
            } else {
                boolean result = commonService.update(CommonSqlManager.updateOrder("", "",
                        outTradeNo, 1, getCurrentTimeMs()));
                if (!result) {
                    return JsonCryptUtil.makeFail(cause);
                }
            }
            JsonObject resultObj = new JsonObject();
            resultObj.addProperty(Column.EXPIRE_TIME, expireTime);
            return JsonCryptUtil.makeSuccess(resultObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonCryptUtil.makeFail(cause);
    }

    /**
     * 是否存在未完成的订单
     * @param outTradeNo
     * @return
     */
    private boolean queryOrderStatus(String outTradeNo) {
        List<Map<String, Object>> listOrder = null;
        try {
            listOrder = commonService.list(CommonSqlManager.queryOrder(outTradeNo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listOrder == null || listOrder.isEmpty()) {
            return false;
        }

        Map<String, Object> goodsData = listOrder.get(0);
        int status = (int) goodsData.get(Column.STATUS);
        return status == 0;
    }

    /**
     * 查询商品会员时长
     * @param appcode
     * @param goodsCode
     * @return
     */
    private long queryGoodsDuration(String appcode, String goodsCode) {
        String tableGoods = TableManager.getGoodsTable(appcode);
        List<Map<String, Object>> listGoods = null;
        try {
            listGoods = commonService.list(CommonSqlManager.queryGoods(tableGoods, goodsCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listGoods == null || listGoods.isEmpty()) {
            return 0;
        }

        Map<String, Object> goodsData = listGoods.get(0);
        Long duration = (Long) goodsData.get(Column.DURATION);
        return duration;
    }

    /**
     * 查询商品代码
     * @param outTradeNo
     * @return
     */
    private String queryGoodsCode(String outTradeNo) {
        List<Map<String, Object>> list = null;
        try {
            list = commonService.list(CommonSqlManager.queryOrder(outTradeNo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return "";
        }
        Map<String, Object> orderMap = list.get(0);
        String goodsCode = (String) orderMap.get(Column.GOODS_CODE);
        return goodsCode;
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
        String appcode = jsonObject.get(Column.APPCODE).getAsString();
        int uid = jsonObject.get(Column.UID).getAsInt();
        String code = jsonObject.get(Column.CODE).getAsString();
        String table = TableManager.getRedeemTable(appcode);

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
        Long duration = (Long) redeem.get(Column.AMOUNT);

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
        map.put(Column.EXPIRE_TIME, expireTime);
        resultList.add(map);
        JsonObject resultObj = new JsonObject();
        resultObj.add(Column.DATA, new Gson().toJsonTree(resultList));
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
        String table = TableManager.getUserTable(appcode);
        try {
            List<Map<String, Object>> list = commonService.list(CommonSqlManager.queryUser(table, uid));
            if (list != null && !list.isEmpty()) {
                Map<String, Object> objectMap = list.get(0);
                long curExpireTime = (Long) objectMap.get(Column.EXPIRE_TIME);
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
