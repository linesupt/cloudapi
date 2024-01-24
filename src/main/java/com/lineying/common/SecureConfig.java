package com.lineying.common;

import com.lineying.annotation.YamlPropertySourceFactory;
import com.lineying.controller.AuthenticationServiceController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = "classpath:conf/secure.yml", encoding = "UTF-8")
@Component
public class SecureConfig {
    // 日志
    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SecureConfig.class);

    // 公钥，用于查找所属项目
    public static String DB_API_KEY = "";
    @Value("${config.db_api_key}")
    public void setDbApiKey(String dbApiKey) {
        DB_API_KEY = dbApiKey;
    }
    // 加密密钥
    public static String DB_SECRET_KEY = "";
    @Value("${config.db_secret_key}")
    public static void setDbSecretKey(String dbSecretKey) {
        DB_SECRET_KEY = dbSecretKey;
    }

    // 首次分块加密偏移量
    public static String IV_SEED = "";
    @Value("${config.iv_seed}")
    public static void setIvSeed(String ivSeed) {
        IV_SEED = ivSeed;
    }

    // 邮箱发送账户名称
    public static String MAIL_SENDER_NAME;
    @Value("${config.mail_sender_name}")
    public static void setMailSenderName(String mailSenderName) {
        MAIL_SENDER_NAME = mailSenderName;
    }

    // 邮箱发送账户
    public static String MAIL_SENDER;
    @Value("${config.mail_sender}")
    public static void setMailSender(String mailSender) {
        MAIL_SENDER = mailSender;
    }

    // 邮箱发送密码
    public static String MAIL_PASSWORD;
    @Value("${config.mail_password}")
    public static void setMailPassword(String mailPassword) {
        MAIL_PASSWORD = mailPassword;
    }

    // 腾讯云授权api
    public static String TENCENTCLOUD_SECRET_ID;
    @Value("${config.tencentcloud_secret_id}")
    public static void setTencentcloudSecretId(String tencentcloudSecretId) {
        TENCENTCLOUD_SECRET_ID = tencentcloudSecretId;
    }

    // 腾讯云授权key
    public static String TENCENTCLOUD_SECRET_KEY;
    @Value("${config.tencentcloud_secret_key}")
    public static void setTencentcloudSecretKey(String tencentcloudSecretKey) {
        TENCENTCLOUD_SECRET_KEY = tencentcloudSecretKey;
    }

    // 支付宝应用公钥
    public static String ALIPAY_APP_PUB_KEY;
    @Value("${pay.alipay.app_pub_key}")
    public static void setAlipayAppPubKey(String alipayAppPubKey) {
        ALIPAY_APP_PUB_KEY = alipayAppPubKey;
    }

    // 支付宝应用私钥
    public static String ALIPAY_APP_PRI_KEY;
    @Value("${pay.alipay.app_pri_key}")
    public static void setAlipayAppPriKey(String alipayAppPriKey) {
        ALIPAY_APP_PRI_KEY = alipayAppPriKey;
    }

    // 支付宝公钥
    public static String ALIPAY_PUB_KEY;
    @Value("${pay.alipay.pub_key}")
    public static void setAlipayPubKey(String alipayPubKey) {
        ALIPAY_PUB_KEY = alipayPubKey;
    }

    // 微信支付商户号
    public static String WXPAY_MERCHANT_ID;
    @Value("${pay.wxpay.merchant_id}")
    public static void setWxpayMerchantId(String wxpayMerchantId) {
        WXPAY_MERCHANT_ID = wxpayMerchantId;
    }

    // 微信支付密钥
    public static String WXPAY_APIV3_KEY;
    @Value("${pay.wxpay.apiv3_key}")
    public static void setWxpayApiv3Key(String wxpayApiv3Key) {
        WXPAY_APIV3_KEY = wxpayApiv3Key;
    }

    // 微信支付私钥路径
    public static String WXPAY_PRI_KEY_PATH;
    @Value("${pay.wxpay.pri_key_path}")
    public static void setWxpayPriKeyPath(String wxpayPriKeyPath) {
        WXPAY_PRI_KEY_PATH = wxpayPriKeyPath;
    }

    // 微信商户序列号
    public static String WXPAY_MERCHANT_SERIAL_NUMBER;
    @Value("${pay.wxpay.merchant_serial_number}")
    public static void setWxpayMerchantSerialNumber(String serialNumber) {
        WXPAY_MERCHANT_SERIAL_NUMBER = serialNumber;
    }

    public static void printInfo() {
        LOGGER.info("secure info::" + DB_API_KEY);
        LOGGER.info(DB_SECRET_KEY + "\n");
        LOGGER.info(IV_SEED + "\n");
        LOGGER.info(MAIL_SENDER_NAME + "\n");
        LOGGER.info(MAIL_SENDER + "\n");
        LOGGER.info(MAIL_PASSWORD + "\n");
        LOGGER.info(TENCENTCLOUD_SECRET_ID + "\n");
        LOGGER.info(TENCENTCLOUD_SECRET_KEY + "\n");
        LOGGER.info(ALIPAY_APP_PUB_KEY + "\n");
        LOGGER.info(ALIPAY_APP_PRI_KEY + "\n");
        LOGGER.info(ALIPAY_PUB_KEY + "\n");
        LOGGER.info(WXPAY_MERCHANT_ID + "\n");
        LOGGER.info(WXPAY_APIV3_KEY + "\n");
        LOGGER.info(WXPAY_PRI_KEY_PATH + "\n");
        LOGGER.info(WXPAY_MERCHANT_SERIAL_NUMBER);
    }

}
