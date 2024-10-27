package com.lineying.controller.api.pay;

import cn.hutool.core.io.FileUtil;
import com.google.gson.JsonObject;
import com.lineying.bean.Order;
import com.lineying.common.CommonConstant;
import com.lineying.common.PayType;
import com.lineying.common.Platform;
import com.lineying.common.SecureConfig;
import com.lineying.controller.BaseController;
import com.lineying.controller.Checker;
import com.lineying.data.Column;
import com.lineying.entity.CommonSqlManager;
import com.lineying.service.ICommonService;
import com.lineying.util.TimeUtil;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import static com.lineying.common.CommonConstant.BASE_URL;

/**
 * 基础支付控制器
 */
public class BasePayController extends BaseController {

    @Resource
    protected ICommonService commonService;

    /**
     * 获取支付宝支付通知地址
     * @return
     */
    protected String getAlipayNotifyUrl() {
        return BASE_URL + CommonConstant.ALIPAY_NOTIFY_URL;
    }

    /**
     * 获取微信支付通知地址
     * @return
     */
    protected String getWxpayNotifyUrl() {
        return BASE_URL + CommonConstant.WXPAY_NOTIFY_URL;
    }

    ////////////////////////// wechat pay ///////////////////////////////
    // 从 v0.2.10 开始，我们不再限制每个商户号只能创建一个 RSAAutoCertificateConfig。
    private RSAAutoCertificateConfig wxpayConfig;
    /** 创建配置 **/
    protected RSAAutoCertificateConfig makeWxpayConfig() throws FileNotFoundException {
        if (wxpayConfig == null) {
            URL url = getClass().getClassLoader().getResource(SecureConfig.WXPAY_PRI_KEY_PATH);
            File file = new File(url.getFile());
            String keyString = FileUtil.readString(url, CommonConstant.CHARSET);
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
     * 创建订单到数据表
     * @param request
     * @return
     */
    protected Order makeOrder(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return null;
        }
        JsonObject jsonObject = pair.getDataObject();
        // 生成订单号
        int platform = Platform.get(request.getHeader(Column.PLATFORM)).getId();
        String payType = jsonObject.get(Column.PAY_TYPE).getAsString();
        String outTradeNo = PayType.get(payType).getId() + platform + TimeUtil.datetimeOrder(getCurrentTimeMs());
        int uid = jsonObject.get(Column.UID).getAsInt();
        String appcode = jsonObject.get(Column.APPCODE).getAsString();
        int goodsId = 0;
        try {
            goodsId = jsonObject.get(Column.GOODS_ID).getAsInt();
        } catch (Exception e) { }
        String goodsCode = jsonObject.get(Column.GOODS_CODE).getAsString();
        String appid = jsonObject.get(Column.APP_ID).getAsString();
        String totalFee = jsonObject.get(Column.TOTAL_FEE).getAsString();
        String body = jsonObject.get(Column.BODY).getAsString();
        Order order = Order.makeOrder(uid, appcode, goodsId, goodsCode, outTradeNo, "", "", body, payType, appid, totalFee);

        boolean result = false;
        try {
            // 保存订单
            result = commonService.add(CommonSqlManager.addColumnData(Order.TABLE, order.getColumn(), order.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            return null;
        }
        return order;
    }

}
