package com.lineying.controller.api.pay;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.TransactionInfoResponse;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import com.apple.itunes.storekit.verification.VerificationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lineying.common.*;
import com.lineying.data.Column;
import com.lineying.data.Param;
import com.lineying.entity.AppEntity;
import com.lineying.entity.CommonSqlManager;
import com.lineying.manager.AppcodeManager;
import com.lineying.manager.TableManager;
import com.lineying.util.*;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.app.model.Transaction;
import com.wechat.pay.java.service.payments.app.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.util.*;

/**
 * 应用级接口
 */
@Component
@RestController
@RequestMapping("api")
public class PayNotifyController extends BasePayController {

    /**
     * 支付宝支付通知
     *
     * @return
     */
    @RequestMapping("/pay/alipay/notify")
    public String alipayNotify(HttpServletRequest request)
            throws AlipayApiException {
        // 支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        Iterator<String> iterator = requestParams.keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, SecureConfig.ALIPAY_PUB_KEY,
                CommonConstant.CHARSET, CommonConstant.SIGN_TYPE); //调用SDK验证签名
        if (signVerified) { // 验证成功
            // 商户订单号
            String outTradeNo = request.getParameter(Column.OUT_TRADE_NO);
            // 支付宝交易号
            String tradeNo = request.getParameter(Column.TRADE_NO);
            // 交易状态
            String tradeStatus = request.getParameter(Column.TRADE_STATUS);
            LOGGER.info("处理支付宝通知!" + outTradeNo
                    + " - " + tradeNo + " - " + tradeStatus);
            int status = 0;
            if (tradeStatus.equals(Param.Trade.FINISHED)) {
                // 判断该笔订单是否在商户网站中已经做过处理
                status = 1;
            } else if (tradeStatus.equals(Param.Trade.SUCCESS)) {
                //判断该笔订单是否在商户网站中已经做过处理
                status = 1;
            }

            handleOrderStatus(tradeNo, outTradeNo, status);
            // 处理订单数据保存
            return "success";
        }

        return "fail";
    }

    /**
     * 处理订单状态
     * @return
     */
    protected boolean handleOrderStatus(String tradeNo, String outTradeNo, int status) {
        boolean result = false;
        try {
            result = commonService.update(CommonSqlManager.updateOrder("", tradeNo, outTradeNo, status, getCurrentTimeMs()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/pay/alipay/return")
    public String alipayReturn(HttpServletRequest request) {
        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 解码
            try {
                params.put(name, URLDecoder.decode(valueStr, CommonConstant.CHARSET));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // 验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, SecureConfig.ALIPAY_PUB_KEY,
                    CommonConstant.CHARSET, CommonConstant.SIGN_TYPE);
            if (signVerified) {
                return Param.Result.SUCCESS;
            } else {
                return Param.Result.FAIL;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Param.Result.FAIL;
        }
    }

    /**
     * 微信支付通知
     * @return
     */
    @RequestMapping("/pay/wxpay/notify")
    public void wxpayNotify(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("接收到微信支付通知!");
        //从请求头获取验签字段
        String timestamp = request.getHeader(Param.Wechatpay.TIMESTAMP);
        // 随机数
        String nonce = request.getHeader(Param.Wechatpay.NONCE);
        // 微信签名
        String signature = request.getHeader(Param.Wechatpay.SIGNATURE);
        // 证书序列号、多个证书的情况下用于查询对应的证书
        String serialNumber = request.getHeader(Param.Wechatpay.SERIAL);
        // 签名方式
        String signType = request.getHeader(Param.Wechatpay.SIGNATURE_TYPE);

        int status = 0;
        String requestBody = "";
        try {
            requestBody = readWXPayData(request);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            try {
                response.getWriter().write(JsonUtil.makeWXPayResult(false));
            } catch (IOException ex) {
                e.printStackTrace();
            }
            return;
        }

        // 构造 RequestParam
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(serialNumber)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .body(requestBody)
                .build();
        // 初始化 NotificationParser
        NotificationParser parser = null;
        try {
            parser = new NotificationParser(makeWxpayConfig());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            try {
                response.getWriter().write(JsonUtil.makeWXPayResult(false));
            } catch (IOException ex) {
                e.printStackTrace();
            }
            return;
        }
        try {
            // 以支付通知回调为例，验签、解密并转换成 Transaction
            Transaction transaction = parser.parse(requestParam, Transaction.class);
            String outTradeNo = transaction.getOutTradeNo();
            // 微信流水号
            String transactionId = transaction.getTransactionId();
            // 附属参数
            String attach = transaction.getAttach();
            Transaction.TradeStateEnum stateEnum = transaction.getTradeState();
            switch (transaction.getTradeState()) {
                case SUCCESS:
                    status = 1;
                    break;
            }
            // 处理成功，返回 200 OK 状态码
            boolean result = handleOrderStatus(transactionId, outTradeNo, status);
            if (!result) {
                // 如果处理失败，应返回 4xx/5xx 的状态码，例如 500 INTERNAL_SERVER_ERROR
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } catch (ValidationException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            LOGGER.error("sign verification failed", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        response.setStatus(HttpStatus.OK.value());
        try {
            response.getWriter().write(JsonUtil.makeWXPayResult(status == 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭订单
     * @param outTradeNo
     * @return
     */
    public String closeOrder(String outTradeNo) {
        CloseOrderRequest closeRequest = new CloseOrderRequest();
        closeRequest.setMchid(SecureConfig.WXPAY_MERCHANT_ID);
        closeRequest.setOutTradeNo(outTradeNo);
        // 方法没有返回值，意味着成功时API返回204 No Content
        // 关闭订单
        // TODO service.closeOrder(closeRequest);
        return JsonCryptUtil.makeSuccess();
    }

    // 发起退款
    public String wxpayRefund() {
        return "";
    }

    /**
     * 读取微信支付通知原始报文
     * @param request
     * @return
     * @throws IOException
     */
    private String readWXPayData(HttpServletRequest request) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * 苹果支付通知
     * @return
     */
    @RequestMapping("/pay/apple/notify")
    public void appleNotify(@RequestBody AppleNotification request) {
        LOGGER.info("接收到苹果订阅通知! apple ios server notification come in, request:{" + JSONObject.toJSONString(request) + "}");
        String signedPayLoad = request.getSignedPayLoad();
        try {
            JSONObject payload = verifyAppleNotify(signedPayLoad);

            String notificationType = payload.get("notificationType").toString();

            JSONObject data = payload.getJSONObject("data");
            LOGGER.info("payload:" + payload);
            LOGGER.info("data:" + data);
            LOGGER.info("apple ios server notification verify success");
            String signedTransactionInfo = data.get("signedTransactionInfo").toString();
            String environment = data.get("environment").toString();

            JSONObject transactionInfo = verifyAppleNotify(signedTransactionInfo);
            String transactionId = transactionInfo.get("transactionId").toString();
            String originalTransactionId = transactionInfo.get("originalTransactionId").toString();
            String productId = transactionInfo.get("productId").toString();
            String appcode = AppcodeManager.getAppcode(productId);
            LOGGER.info("apple pay=====>>" + environment + " - " + transactionId + " - " + originalTransactionId + " - " + productId);
            switch (notificationType) {
                case AppleNotificationType.DID_RENEW: // 处理订阅续期业务逻辑
                    doHandleTransaction(appcode, "", productId, transactionId, originalTransactionId);
                    break;
                case AppleNotificationType.REFUND: // 处理退款业务逻辑
                    LOGGER.error("未处理:退款业务");
                    break;
                default:
                    LOGGER.error("notificationType:{" + notificationType + "}未处理");
                    break;
            }
        } catch (CertificateException e) {
            LOGGER.error("apple server notification verify error, signedPayLoad:{" + signedPayLoad + " - " + e.getMessage() + "}");
        }
    }

    /**
     * 验证苹果支付通知
     * @param jws
     * @return
     * @throws CertificateException
     */
    public static JSONObject verifyAppleNotify(String jws) throws CertificateException {
        DecodedJWT decodedJWT = JWT.decode(jws);
        // 拿到 header 中 x5c 数组中第一个
        String header = new String(java.util.Base64.getDecoder().decode(decodedJWT.getHeader()));
        String x5c = JSONObject.parseObject(header).getJSONArray("x5c").getString(0);
        // 获取公钥
        PublicKey publicKey = getPublicKeyByX5c(x5c);
        // 验证 token
        Algorithm algorithm = Algorithm.ECDSA256((ECPublicKey) publicKey, null);
        try {
            algorithm.verify(decodedJWT);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("签名验证失败");
        }
        // 解析数据
        return JSONObject.parseObject(new String(java.util.Base64.getDecoder().decode(decodedJWT.getPayload())));
    }

    /**
     * 获取公钥
     * @param x5c
     * @return
     * @throws CertificateException
     */
    private static PublicKey getPublicKeyByX5c(String x5c) throws CertificateException {
        byte[] x5c0Bytes = java.util.Base64.getDecoder().decode(x5c);
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        X509Certificate cer = (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(x5c0Bytes));
        return cer.getPublicKey();
    }

    /**
     * 处理数据库查询
     * @param appcode
     * @param outTradeNo
     * @param productId
     * @param transactionId
     * @param originalTransactionId
     * @return
     */
    public long doHandleTransaction(String appcode, String outTradeNo, String productId, String transactionId, String originalTransactionId) {

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
    }

    /**
     * 服务器时间戳（s）
     * @return
     */
    @RequestMapping("/timestamp2")
    public long timestamp() {
        /*try {
            // 沙盒环境
            JWSTransactionDecodedPayload p0 = getTransactionFromApple("mathcalc", "2000000743714173");
            LOGGER.info("p0::" + p0);
            // 生产环境
            JWSTransactionDecodedPayload p1 = getTransactionFromApple("mathcalc", "2024092722001460861412318573");
            LOGGER.info("p1::" + p1);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 解签交易信息
     * @param transactionId
     * @throws IOException
     * @throws APIException
     * @throws VerificationException
     * @throws VerificationException
     */
    private JWSTransactionDecodedPayload getTransactionFromApple(String appcode, String transactionId) throws IOException, APIException, VerificationException, VerificationException {

        AppEntity entity = AppcodeManager.getEntity(appcode);
        if (entity == null) {
            return null;
        }
        String bundleId = entity.getBundleId();
        long appleId = entity.getAppleId();

        String keyId = SecureConfig.APPLE_KEY_ID;
        String issuerId = SecureConfig.APPLE_ISSUER_ID;

        ClassLoader loader = getClass().getClassLoader();
        URL authKeyPath = loader.getResource(SecureConfig.APPLE_AUTH_KEY_PATH);
        URL comRootCert = loader.getResource(SecureConfig.APPLE_COMPUTER_ROOT_CERT_PATH);
        URL incRootCert = loader.getResource(SecureConfig.APPLE_INC_ROOT_CERT_PATH);
        URL rootCaG2 = loader.getResource(SecureConfig.APPLE_ROOT_CA_G2);
        URL rootCaG3 = loader.getResource(SecureConfig.APPLE_ROOT_CA_G3);
        LOGGER.info("authKeyPath::" + authKeyPath);
        LOGGER.info("authKeyPath2::" + authKeyPath.getPath());
        LOGGER.info("comRootCert::" + comRootCert);
        LOGGER.info("comRootCert2::" + comRootCert.getPath());
        LOGGER.info("incRootCert::" + incRootCert);
        LOGGER.info("incRootCert2::" + incRootCert.getPath());
        LOGGER.info("rootCaG2::" + rootCaG2);
        LOGGER.info("rootCaG2-2::" + rootCaG2.getPath());
        LOGGER.info("rootCaG3::" + rootCaG3);
        LOGGER.info("rootCaG3-2::" + rootCaG3.getPath());
        String encodedKey = FileUtil.readString(authKeyPath, CommonConstant.CHARSET);
        Environment environment = Environment.SANDBOX;
        Set<InputStream> rootCAs =  Set.of(
                new FileInputStream(comRootCert.getPath()),
                new FileInputStream(incRootCert.getPath()),
                new FileInputStream(rootCaG2.getPath()),
                new FileInputStream(rootCaG3.getPath())
        );

        //创建appleStoreServer对象
        AppStoreServerAPIClient client = new AppStoreServerAPIClient(encodedKey, keyId, issuerId, bundleId, environment);
        //根据传输的订单号获取订单信息
        TransactionInfoResponse sendResponse = client.getTransactionInfo(transactionId);
        Boolean onlineChecks = false ;
        SignedDataVerifier signedDataVerifier = new SignedDataVerifier(rootCAs, bundleId, appleId, environment, onlineChecks);
        String signedPayLoad = sendResponse.getSignedTransactionInfo();
        //对订单信息进行解析得到订单信息
        JWSTransactionDecodedPayload payload = signedDataVerifier.verifyAndDecodeTransaction(signedPayLoad);
        //进行订单信息处理
        System.out.println("payload::" + signedPayLoad + "\n" + payload);
        return payload;
    }

}
