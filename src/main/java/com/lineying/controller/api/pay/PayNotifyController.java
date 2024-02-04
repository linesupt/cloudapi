package com.lineying.controller.api.pay;

import cn.hutool.core.io.FileUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.bean.Order;
import com.lineying.common.PayType;
import com.lineying.common.Platform;
import com.lineying.common.SecureConfig;
import com.lineying.controller.BaseController;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonUpdateEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.*;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.app.model.Transaction;
import com.wechat.pay.java.service.payments.app.AppServiceExtension;
import com.wechat.pay.java.service.payments.app.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import static com.lineying.common.CommonConstant.BASE_URL;
import static com.lineying.common.SignResult.KEY_ERROR;
import static com.lineying.common.SignResult.SIGN_ERROR;

/**
 * 应用级接口
 */
@Component
@RestController
@RequestMapping("api")
public class PayNotifyController extends BaseController {

    @Resource
    ICommonService commonService;

    // 签名方式
    public static final String SIGN_TYPE = "RSA2";
    // 数据格式
    public static final String FORMAT = "json";
    // 订单超时时间
    public static final String TIMEOUT = "15m";

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
        boolean signVerified = AlipaySignature.rsaCheckV1(params, SecureConfig.ALIPAY_PUB_KEY, CHARSET, SIGN_TYPE); //调用SDK验证签名
        if (signVerified) { // 验证成功
            // 商户订单号
            String out_trade_no = request.getParameter("out_trade_no");
            // 支付宝交易号
            String trade_no = request.getParameter("trade_no");
            // 交易状态
            String trade_status = request.getParameter("trade_status");
            Logger.getGlobal().info("处理支付宝通知!" + out_trade_no
                    + " - " + trade_no + " - " + trade_status);
            int status = 0;
            if (trade_status.equals("TRADE_FINISHED")) {
                // 判断该笔订单是否在商户网站中已经做过处理
                status = 1;
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                status = 1;
            }

            CommonUpdateEntity entity = new CommonUpdateEntity();
            entity.setSet(String.format("trade_no='%s', status='%s', update_time='%s'", trade_no, status + "", getCurrentTimeMs()));
            entity.setWhere(String.format("out_trade_no=%s", out_trade_no));
            entity.setTable(Order.TABLE);
            boolean result = false;
            try {
                result = commonService.update(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 处理订单数据保存
            return "success";
        }

        return "fail";
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
                params.put(name, URLDecoder.decode(valueStr, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // 验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, SecureConfig.ALIPAY_PUB_KEY, CHARSET, SIGN_TYPE);
            if (signVerified) {
                return "success";
            } else {
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "fail";
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
     * 微信支付通知
     * @return
     */
    @RequestMapping("/pay/wxpay/notify")
    public void wxpayNotify(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("接收到微信支付通知!");
        //从请求头获取验签字段
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        // 随机数
        String nonce = request.getHeader("Wechatpay-Nonce");
        // 微信签名
        String signature = request.getHeader("Wechatpay-Signature");
        // 证书序列号、多个证书的情况下用于查询对应的证书
        String serialNumber = request.getHeader("Wechatpay-Serial");
        // 签名方式
        String signType = request.getHeader("Wechatpay-Signature-Type");

        int status = 0;
        String requestBody = "";
        try {
            requestBody = readReqData(request);
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

        HashMap<String, String> map = new HashMap<>();
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
            CommonUpdateEntity entity = new CommonUpdateEntity();
            entity.setSet(String.format("trade_no='%s', status='%s', update_time='%s'", transactionId, status + "", getCurrentTimeMs()));
            entity.setWhere(String.format("out_trade_no=%s", outTradeNo));
            entity.setTable(Order.TABLE);
            boolean result = false;
            try {
                result = commonService.update(entity);
            } catch (Exception e) {
                e.printStackTrace();
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
    public String refund() {
        return "";
    }

    /**
     * 读取请求原始报文
     * @param request
     * @return
     * @throws IOException
     */
    private String readReqData(HttpServletRequest request) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}
