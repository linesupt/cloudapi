package com.lineying.controller.api.pay;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.cert.TrustAnyHostnameVerifier;
import com.lineying.cert.TrustAnyTrustManager;
import com.lineying.common.*;
import com.lineying.controller.Checker;
import com.lineying.data.Column;
import com.lineying.entity.CommonSqlManager;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.JsonUtil;
import com.lineying.util.TextUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * 苹果收据验证
 */
@Component
@RestController
@RequestMapping("api")
public class AppleReceiptVerifyController extends BasePayController {

    /**
     * 苹果服务器验证收据
     * 客户端主动调用
     * @param request 收据
     * @return
     */
    @RequestMapping("/pay/apple/receipt_verify")
    public String receiptVerify(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String locale = pair.getLocale();
        String appcode = pair.getAppcode();

        String receipt = jsonObject.get(Column.RECEIPT).getAsString();
        String outTradeNo = jsonObject.get(Column.OUT_TRADE_NO).getAsString();
        boolean isSandbox = jsonObject.get(Column.SANDBOX).getAsInt() == 1;
        //isSandbox = false;
        String verifyUrl = CommonConstant.VERIFY_RECEIPT_BUY;
        if (isSandbox) {
            verifyUrl = CommonConstant.VERIFY_RECEIPT_SANDBOX;
        }
        LOGGER.info("receiptVerify::" + outTradeNo + " isSandbox:" + isSandbox + "\n" + verifyUrl + "\n" + receipt);
        String secret = SecretManager.getSecret(appcode);
        String result = doVerify(verifyUrl, receipt, secret);
        LOGGER.info("verify result::" + result);
        if (TextUtils.isEmpty(result)) {
            return JsonCryptUtil.makeFail("verify result empty", ErrorCode.RECEIPT_VERIFY_EMPTY);
        }
        JsonObject resultObject = JsonParser.parseString(result).getAsJsonObject();
        int status = resultObject.get(Column.STATUS).getAsInt();
        LOGGER.info("verify status::" + status + "\n" + resultObject);
        /**
         * 请先使用生产 URL 验证您的收据；如果收到 21007 状态代码，
         * 再使用沙盒 URL 进行验证。
         * 这种方法可以确保您不必在 app 的测试期间、App Review 审核期间或已在 App Store 上架后切换 URL。
         */
        if (status == ReceiptCode.STATUS_OK) {
            // 处理业务
            long expireTime = handleTransaction(appcode, locale, outTradeNo, resultObject);
            if (expireTime <= 0) {
                return JsonCryptUtil.makeFail("Inner error", ErrorCode.FAIL);
            }
            JsonObject resultObj = new JsonObject();
            resultObj.addProperty(Column.EXPIRE_TIME, expireTime);
            return JsonCryptUtil.makeSuccess(resultObj);
        }
        return JsonCryptUtil.makeFail("Receipt is invalid");
    }


    /**
     * 处理交易逻辑
     * 查询/保存订单到数据库
     * @param appcode 对应产品
     * @param locale 对应语言环境
     * @param jsonObject
     * @return 返回过期时间，大于0表示成功
     */
    public long handleTransaction(String appcode, String locale, String outTradeNo, JsonObject jsonObject) {
        try {
            JsonObject receiptJson = jsonObject.get("receipt").getAsJsonObject();
            JsonArray inAppArray = receiptJson.get("in_app").getAsJsonArray();
            if (inAppArray.isEmpty()) {
                return 0;
            }
            List<InAppItem> results = new ArrayList<>();
            for (int i = 0; i < inAppArray.size(); i++) {
                JsonObject objItem = inAppArray.get(i).getAsJsonObject();
                InAppItem model = new Gson().fromJson(objItem, InAppItem.class);
                results.add(model);
            }
            LOGGER.info("results--->>::" + results.size() + " - " + results);
            // 获取最新一条交易数据
            Collections.sort(results);
            LOGGER.info("results::" + results.size() + " - " + results);
            InAppItem target = results.get(0);
            String productId = target.getProductId();
            String transactionId = target.getTransactionId(); // 订单号
            String originalTransactionId = target.getOriginalTransactionId(); // 原始订单号
            //获取商品id和订单号以后 此处可以处理本地的订单逻辑
            LOGGER.info("获取到产品id:" + productId + " 订单号:" + transactionId + " - " + originalTransactionId);
            List<Map<String, Object>> appleOrderList = commonService.list(CommonSqlManager.queryOrderForTradeNo(transactionId));
            if (appleOrderList != null) {
                if (appleOrderList.size() == 1) { // 判断状态
                    Map<String, Object> orderMap = appleOrderList.get(0);
                    // 交易状态
                    int tradeStatus = (Integer) orderMap.get(Column.STATUS);
                    if (tradeStatus == 1) { // 订单已经处理过
                        LOGGER.info("订单号已经处理过::" + transactionId);
                        return 0;
                    }
                } else if (appleOrderList.size() > 1) { // 交易出错，订单号重复
                    LOGGER.info("交易出错，订单号重复::" + transactionId);
                    return 0;
                }
            }

            List<Map<String, Object>> orderList = commonService.list(CommonSqlManager.queryOrder(outTradeNo));
            if (orderList == null || orderList.size() != 1) {
                return 0;
            }
            Map<String, Object> orderMap = orderList.get(0);

            String table = TableManager.getUserTable(appcode);
            String tableGoods = TableManager.getGoodsTable(appcode);
            // 交易状态
            int tradeStatus = (Integer) orderMap.get(Column.STATUS);
            if (tradeStatus == 1) { // 订单已经处理过
                LOGGER.info("订单号已经处理过 自订单号::" + outTradeNo);
                return 0;
            }
            String goodsCode = (String) orderMap.get(Column.GOODS_CODE);
            int uid = (Integer) orderMap.get(Column.UID);
            List<Map<String, Object>> goodsList = commonService.list(CommonSqlManager.queryGoods(tableGoods, goodsCode));
            if (goodsList == null || goodsList.isEmpty()) {
                return 0;
            }
            // 获取商品
            Map<String, Object> goodsMap = goodsList.get(0);
            long duration = (Long) goodsMap.get(Column.DURATION); //
            List<Map<String, Object>> userList = commonService.list(CommonSqlManager.queryUser(table, uid));
            if (userList == null || userList.size() != 1) {
                return 0;
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
                return 0;
            }
            // 更新订单状态
            result = commonService.update(CommonSqlManager.updateOrder(originalTransactionId, transactionId, outTradeNo, 1, getCurrentTimeMs()));
            if (result) {
                return expireTime;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 执行验证代码，获取收据内容
     * @param url
     * @param receipt
     * @param secret apple 共享密钥
     * @return
     */
    private String doVerify(String url, String receipt, String secret) {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "text/json");
            conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedOutputStream hurlBufOus = new BufferedOutputStream(conn.getOutputStream());
            Map<String, Object> data = new HashMap<>();
            data.put("receipt-data", receipt);
            data.put("password", secret);
            String text = JsonUtil.makeData(data);
            LOGGER.info("body::" + text + "\n" + data);
            hurlBufOus.write(text.getBytes());
            hurlBufOus.flush();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            LOGGER.error("苹果服务器验证出错:{" + e.getMessage() + "}");
        }

        return null;
    }

}
