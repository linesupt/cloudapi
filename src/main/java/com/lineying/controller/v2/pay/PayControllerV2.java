package com.lineying.controller.v2.pay;

import cn.hutool.core.io.FileUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.bean.Order;
import com.lineying.common.AppCodeManager;
import com.lineying.common.PayType;
import com.lineying.common.Platform;
import com.lineying.common.SecureConfig;
import com.lineying.controller.BaseController;
import com.lineying.controller.Checker;
import com.lineying.controller.api.pay.PayNotifyController;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonQueryEntity;
import com.lineying.entity.CommonUpdateEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.*;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.app.AppServiceExtension;
import com.wechat.pay.java.service.payments.app.model.Amount;
import com.wechat.pay.java.service.payments.app.model.PrepayRequest;
import com.wechat.pay.java.service.payments.app.model.PrepayWithRequestPaymentResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.lineying.common.CommonConstant.BASE_URL;

/**
 * 应用级接口
 */
@Component
@RestController
@RequestMapping("v2")
public class PayControllerV2 extends BaseController {

    @Resource
    ICommonService commonService;

    // 数据格式
    public static final String FORMAT = "json";
    // 订单超时时间
    public static final String TIMEOUT = "15m";

    /**
     * 创建订单
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

    // 创建订单
    private Order makeOrder(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return null;
        }
        JsonObject jsonObject = pair.getDataObject();
        // 生成订单号
        int platform = Platform.get(request.getHeader("platform")).getId();
        String payType = jsonObject.get("pay_type").getAsString();
        String outTradeNo = PayType.get(payType).getId() + platform + TimeUtil.datetimeOrder(getCurrentTimeMs());
        int uid = jsonObject.get("uid").getAsInt();
        String appcode = jsonObject.get("appcode").getAsString();
        String goodsCode = jsonObject.get("goods_code").getAsString();
        String appid = jsonObject.get("app_id").getAsString();
        String totalFee = jsonObject.get("total_fee").getAsString();
        String body = jsonObject.get("body").getAsString();
        Order order = Order.makeOrder(uid, appcode, goodsCode, outTradeNo, body, payType, appid, totalFee);

        CommonAddEntity entity = new CommonAddEntity();
        entity.setTable(Order.TABLE);
        entity.setColumn(order.getColumn());
        entity.setValue(order.getValue());
        boolean result = false;
        try {
            // 保存订单
            result = commonService.add(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            return null;
        }
        return order;
    }

    /**
     * 创建支付宝支付信息
     *
     * @return
     */
    @RequestMapping("/pay/alipay/mkpay")
    public String alipayAppPay(HttpServletRequest request) {

        Order order = makeOrder(request);
        if (order == null) {
            return JsonCryptUtil.makeFail("create order fail");
        }

        Logger.getGlobal().info("处理支付宝支付!" + order);
        // 实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(PayNotifyController.GATEWAY_URL, order.getAppid(), SecureConfig.ALIPAY_APP_PRI_KEY,
                FORMAT, CHARSET, SecureConfig.ALIPAY_PUB_KEY, PayNotifyController.SIGN_TYPE);
        // 实例化请求对象
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
        alipayRequest.setNotifyUrl(BASE_URL + PayNotifyController.ALIPAY_NOTIFY_URL);
        // 设置订单信息
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(order.getOutTradeNo());
        model.setSubject(order.getBody());
        model.setTotalAmount(order.getTotalFee());
        model.setBody(order.getBody());
        model.setTimeoutExpress(TIMEOUT);
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayRequest.setBizModel(model);
        try {
            AlipayTradeAppPayResponse resp = alipayClient.sdkExecute(alipayRequest);
            String respResult = resp.getBody();
            JsonObject resultObj = new JsonObject();
            resultObj.addProperty("trade_no", order.getOutTradeNo());
            resultObj.addProperty("order_info", respResult);
            return JsonCryptUtil.makeSuccess(resultObj);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
    }

    ////////////////////////// wechat pay ///////////////////////////////
    // 从 v0.2.10 开始，我们不再限制每个商户号只能创建一个 RSAAutoCertificateConfig。
    private RSAAutoCertificateConfig wxpayConfig;
    /** 创建配置 **/
    private RSAAutoCertificateConfig makeWxpayConfig() throws FileNotFoundException {
        if (wxpayConfig == null) {
            URL url = getClass().getClassLoader().getResource(SecureConfig.WXPAY_PRI_KEY_PATH);
            File file = new File(url.getFile());
            String keyString = FileUtil.readString(url, "utf-8");
            //File file = ResourceUtils.getFile("classpath:" + SecureConfig.WXPAY_PRI_KEY_PATH);
            //String path = file.getAbsolutePath();
            //LOGGER.info("私钥key::" + keyString);
            wxpayConfig = new RSAAutoCertificateConfig.Builder()
                    .merchantId(SecureConfig.WXPAY_MERCHANT_ID)
                    //.privateKeyFromPath(path)
                    .privateKey(keyString)
                    .merchantSerialNumber(SecureConfig.WXPAY_MERCHANT_SERIAL_NUMBER)
                    .apiV3Key(SecureConfig.WXPAY_APIV3_KEY)
                    .build();
        }
        return wxpayConfig;
    }

    /**
     * 创建微信支付信息（APP）
     * @return 返回客户端唤醒微信支付的方法
     */
    @RequestMapping("/pay/wxpay/mkpay")
    public String wxpayAppPay(HttpServletRequest request) {

        Order order = makeOrder(request);
        if (order == null) {
            return JsonCryptUtil.makeFail("create order fail");
        }
        LOGGER.info("处理微信支付!" + order);

        // 单位为分
        int total = (int) Math.round(Double.parseDouble(order.getTotalFee()) * 100);
        // 构建service
        AppServiceExtension service = null;
        try {
            service = new AppServiceExtension.Builder()
                    .config(makeWxpayConfig()).build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        PrepayRequest prepayRequest = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(total);
        prepayRequest.setAmount(amount);
        prepayRequest.setAppid(order.getAppid());
        prepayRequest.setMchid(SecureConfig.WXPAY_MERCHANT_ID);
        prepayRequest.setDescription(order.getBody());
        prepayRequest.setNotifyUrl(BASE_URL + PayNotifyController.WXPAY_NOTIFY_URL);
        prepayRequest.setOutTradeNo(order.getOutTradeNo());

        // 调用下单方法，得到应答
        PrepayWithRequestPaymentResponse response = service.prepayWithRequestPayment(prepayRequest);
        String appId = response.getAppid();
        String partnerId = response.getPartnerId();
        String prepayId = response.getPrepayId();
        String nonceStr = response.getNonceStr();
        String timestampRep = response.getTimestamp();
        String packageVal = response.getPackageVal();
        String sign = response.getSign();
        LOGGER.info("sign::" + sign);
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("out_trade_no", order.getOutTradeNo());
        resultObj.addProperty("appid", appId);
        resultObj.addProperty("partnerid", partnerId);
        resultObj.addProperty("prepayid", prepayId);
        resultObj.addProperty("noncestr", nonceStr);
        resultObj.addProperty("timestamp", timestampRep);
        resultObj.addProperty("package", packageVal);
        resultObj.addProperty("sign", sign);
        return JsonCryptUtil.makeSuccess(resultObj);
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

        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn("*");
        entity.setSort("desc");
        entity.setSortColumn("id");

        // 查询是否存在未使用的兑换码
        String where = String.format("code='%s' and appcode='%s' and status='%s'", code, appcode, 0 + "");
        entity.setWhere(where);

        List<Map<String, Object>> list = null;
        try {
            list = commonService.list(entity);
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
            CommonUpdateEntity updateEntity = new CommonUpdateEntity();
            updateEntity.setTable(table);
            updateEntity.setSet("status='" + 1 + "'");
            updateEntity.setWhere(where);
            boolean result = commonService.update(updateEntity);
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
        String where = "id='" + uid + "'";
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        try {
            entity.setColumn("*");
            entity.setSort("desc");
            entity.setSortColumn("id");
            List<Map<String, Object>> list = commonService.list(entity);
            if (list != null && !list.isEmpty()) {
                Map<String, Object> objectMap = list.get(0);
                long curExpireTime = (Long) objectMap.get("expire_time");
                long expireTime = getCurrentTimeMs();
                if (curExpireTime < getCurrentTimeMs()) {
                    expireTime = getCurrentTimeMs() + durationAdd;
                } else if (curExpireTime >= getCurrentTimeMs()) {
                    expireTime = curExpireTime + durationAdd;
                }
                // 更新时间
                CommonUpdateEntity updateEntity = new CommonUpdateEntity();
                updateEntity.setTable(table);
                updateEntity.setSet("expire_time='" + expireTime + "'");
                updateEntity.setWhere(where);
                boolean result = commonService.update(updateEntity);
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
