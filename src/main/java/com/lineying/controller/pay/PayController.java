package com.lineying.controller.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.lineying.controller.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 应用级接口
 */
@Component
@RestController
@RequestMapping("api")
public class PayController extends BaseController {

    @Value("${pay.app_pub_key}") // 应用公钥
    private String appPubKey;

    @Value("${pay.app_pri_key}") // 应用私钥
    private String appPriKey;

    @Value("${pay.alipay.pub_key}") // 支付宝公钥
    private String alipayPubKey;

    // 支付宝网关,注意这些使用的是沙箱的支付宝网关，与正常网关的区别是多了dev
    public static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    public static final String GATEWAY_URL_DEV = "https://openapi.alipaydev.com/gateway.do";
    // 通知地址
    public static final String RETURN_URL = "cloud/api/pay/alipay/return";
    public static final String NOTIFY_URL = "cloud/api/pay/alipay/notify";

    // 签名方式
    public static final String SIGN_TYPE = "RSA2";
    // 数据格式
    public static final String FORMAT = "json";

    /**
     * 创建支付宝支付信息
     *
     * @return
     */
    @RequestMapping("/pay/alipay/mkpay")
    public String alipayAppPay(HttpServletRequest request) throws AlipayApiException {

        String app_id = request.getParameter("app_id");
        String out_trade_no = request.getParameter("out_trade_no");
        String total_fee = request.getParameter("total_fee");
        // String mch_id = request.getParameter("mch_id");
        String body = request.getParameter("body");

        Logger.getGlobal().info("处理支付宝支付!" + app_id + " - " + out_trade_no + " - " + total_fee + " - " + body);

        // 实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, app_id, appPriKey, FORMAT, CHARSET, alipayPubKey, SIGN_TYPE);
        // 实例化请求对象
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        /// 设置回调地址
        alipayRequest.setReturnUrl(request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + "/" + RETURN_URL);
        alipayRequest.setNotifyUrl(request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + "/" + NOTIFY_URL);
        // 设置订单信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(body);
        model.setTotalAmount(total_fee);
        model.setBody(body);
        model.setTimeoutExpress("30m");
        model.setProductCode("QUICK_WAP_WAY");
        alipayRequest.setBizModel(model);
        // 调用SDK生成表单
        String form = alipayClient.sdkExecute(alipayRequest).getBody();
        return form;
    }

    @RequestMapping("/pay/alipay/return")
    public String alipayReturn(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
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
            params.put(name, URLDecoder.decode(valueStr, "UTF-8"));
        }
        // 验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayPubKey, CHARSET, SIGN_TYPE);
        if (signVerified) {
            return "success";
        } else {
            return "fail";
        }
    }

    /**
     * 支付宝支付通知
     *
     * @return
     */
    @RequestMapping("/pay/alipay/notify")
    public String alipayNotify(HttpServletRequest request)
            throws AlipayApiException {
        // TODO 支付宝通知
        Logger.getGlobal().info("处理支付宝通知!");

        //支付宝POST过来反馈信息
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
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayPubKey, CHARSET, SIGN_TYPE); //调用SDK验证签名
        if (signVerified) { // 验证成功
            // 商户订单号
            String out_trade_no = request.getParameter("out_trade_no");
            // 支付宝交易号
            String trade_no = request.getParameter("trade_no");
            // 交易状态
            String trade_status = request.getParameter("trade_status");
            Logger.getGlobal().info("处理支付宝通知!" + out_trade_no
                    + " - " + trade_no + " - " + trade_status);
            if (trade_status.equals("TRADE_FINISHED")) {
                // 判断该笔订单是否在商户网站中已经做过处理
                // 判断
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
            }
            // 处理订单数据保存
            return "success";
        }

        return "fail";
    }

    /**
     * 创建微信支付信息
     *
     * @return
     */
    @RequestMapping("/pay/wxpay/mkpay")
    public String wxpayAppPay(HttpServletRequest request) {
        String app_id = request.getParameter("app_id");
        String out_trade_no = request.getParameter("out_trade_no");
        String total_fee = request.getParameter("total_fee");
        String mch_id = request.getParameter("mch_id");
        String body = request.getParameter("body");
        String sign = request.getParameter("sign");

        Logger.getGlobal().info("处理微信支付!" + app_id + " - " + out_trade_no + " - " + total_fee
                + " - " + mch_id + " - " + body + " - " + sign);
        return "wxpay app pay";
    }

    /**
     * 微信支付通知
     *
     * @return
     */
    @RequestMapping("/pay/wxpay/notify")
    public String wxpayNotify(HttpServletRequest request) {
        // TODO 微信通知
        Logger.getGlobal().info("处理微信通知!");
        return "wxpay notify";
    }

}
