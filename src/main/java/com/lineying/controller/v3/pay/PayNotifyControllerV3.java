package com.lineying.controller.v3.pay;

import com.alipay.api.AlipayApiException;
import com.lineying.common.TableManager;
import com.lineying.controller.api.pay.PayNotifyController;
import com.lineying.data.Column;
import com.lineying.entity.CommonSqlManager;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 应用级接口
 */
@Component
@RestController
@RequestMapping("v3")
public class PayNotifyControllerV3 extends PayNotifyController {

    /**
     * 支付宝支付通知
     * @return
     */
    @RequestMapping("/pay/alipay/notify")
    public String alipayNotify(HttpServletRequest request)
            throws AlipayApiException {
        return super.alipayNotify(request);
    }

    /**
     * 微信支付通知
     * @return
     */
    @RequestMapping("/pay/wxpay/notify")
    public void wxpayNotify(HttpServletRequest request, HttpServletResponse response) {
        super.wxpayNotify(request, response);
    }

    /**
     * 处理订单状态，自动更新会员时长
     * @param tradeNo
     * @param outTradeNo
     * @param status
     * @return
     */
    @Override
    protected boolean handleOrderStatus(String tradeNo, String outTradeNo, int status) {

        try {
            List<Map<String, Object>> orderList = commonService.list(CommonSqlManager.queryOrder(outTradeNo));
            if (orderList == null || orderList.size() != 1) {
                return false;
            }
            Map<String, Object> orderMap = orderList.get(0);
            String appcode = (String) orderMap.get(Column.APPCODE);
            String table = TableManager.getUserTable(appcode);
            String tableGoods = TableManager.getGoodsTable(appcode);
            String goodsCode = (String) orderMap.get(Column.GOODS_CODE);
            int uid = (Integer) orderMap.get(Column.UID);

            List<Map<String, Object>> goodsList = commonService.list(CommonSqlManager.queryGoods(tableGoods, goodsCode));
            if (goodsList == null || goodsList.isEmpty()) {
                return false;
            }
            Map<String, Object> goodsMap = goodsList.get(0);
            long duration = (Long) goodsMap.get(Column.DURATION);

            List<Map<String, Object>> userList = commonService.list(CommonSqlManager.queryUser(table, uid));
            if (userList == null || userList.size() != 1) {
                return false;
            }
            Map<String, Object> userMap = userList.get(0);
            long expireTime = (Long) userMap.get(Column.EXPIRE_TIME);
            // 计算出会员过期时长
            if (expireTime > getCurrentTimeMs()) {
                expireTime += duration;
            } else {
                expireTime = getCurrentTimeMs() + duration;
            }
            // 更新用户会员时间
            boolean result = commonService.update(CommonSqlManager.updateAttr(table, uid, Column.EXPIRE_TIME, expireTime + ""));
            if (!result) {
                return false;
            }
            // 更新订单状态
            result = commonService.update(CommonSqlManager.updateOrder(tradeNo, outTradeNo, status, getCurrentTimeMs()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
