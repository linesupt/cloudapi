package com.lineying.common;

import com.lineying.annotation.YamlPropertySourceFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = "classpath:conf/secure.yml",
        factory = YamlPropertySourceFactory.class, encoding = "UTF-8")
@Component
public class SecureConfig {
    // 日志
    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SecureConfig.class);

    // 公钥，用于查找所属项目
    public static String DB_API_KEY;
    @Value("${config.db_api_key}")
    public void setDbApiKey(String dbApiKey) {
        DB_API_KEY = dbApiKey;
    }
    // 加密密钥
    public static String DB_SECRET_KEY;
    @Value("${config.db_secret_key}")
    public void setDbSecretKey(String dbSecretKey) {
        DB_SECRET_KEY = dbSecretKey;
    }

    // 首次分块加密偏移量
    public static String IV_SEED;
    @Value("${config.iv_seed}")
    public void setIvSeed(String ivSeed) {
        IV_SEED = ivSeed;
    }

    // 邮箱发送账户名称
    public static String MAIL_SENDER_NAME;
    @Value("${config.mail_sender_name}")
    public void setMailSenderName(String mailSenderName) {
        MAIL_SENDER_NAME = mailSenderName;
    }

    // 邮箱发送账户
    public static String MAIL_SENDER;
    @Value("${config.mail_sender}")
    public void setMailSender(String mailSender) {
        MAIL_SENDER = mailSender;
    }

    // 邮箱发送密码
    public static String MAIL_PASSWORD;
    @Value("${config.mail_password}")
    public void setMailPassword(String mailPassword) {
        MAIL_PASSWORD = mailPassword;
    }

    // 腾讯云授权api
    public static String TENCENTCLOUD_SECRET_ID;
    @Value("${tencentcloud.secret_id}")
    public void setTencentcloudSecretId(String tencentcloudSecretId) {
        TENCENTCLOUD_SECRET_ID = tencentcloudSecretId;
    }

    // 腾讯云授权key
    public static String TENCENTCLOUD_SECRET_KEY;
    @Value("${tencentcloud.secret_key}")
    public void setTencentcloudSecretKey(String tencentcloudSecretKey) {
        TENCENTCLOUD_SECRET_KEY = tencentcloudSecretKey;
    }

    // 支付宝应用公钥
    public static String ALIPAY_APP_PUB_KEY;
    @Value("${pay.alipay.app_pub_key}")
    public void setAlipayAppPubKey(String alipayAppPubKey) {
        ALIPAY_APP_PUB_KEY = alipayAppPubKey;
    }

    // 支付宝应用私钥
    public static String ALIPAY_APP_PRI_KEY;
    @Value("${pay.alipay.app_pri_key}")
    public void setAlipayAppPriKey(String alipayAppPriKey) {
        ALIPAY_APP_PRI_KEY = alipayAppPriKey;
    }

    // 支付宝公钥
    public static String ALIPAY_PUB_KEY;
    @Value("${pay.alipay.pub_key}")
    public void setAlipayPubKey(String alipayPubKey) {
        ALIPAY_PUB_KEY = alipayPubKey;
    }

    // 微信支付商户号
    public static String WXPAY_MERCHANT_ID;
    @Value("${pay.wxpay.merchant_id}")
    public void setWxpayMerchantId(String wxpayMerchantId) {
        WXPAY_MERCHANT_ID = wxpayMerchantId;
    }

    // 微信支付密钥
    public static String WXPAY_APIV3_KEY;
    @Value("${pay.wxpay.apiv3_key}")
    public void setWxpayApiv3Key(String wxpayApiv3Key) {
        WXPAY_APIV3_KEY = wxpayApiv3Key;
    }

    // 微信支付私钥路径
    public static String WXPAY_PRI_KEY_PATH;
    @Value("${pay.wxpay.pri_key_path}")
    public void setWxpayPriKeyPath(String wxpayPriKeyPath) {
        WXPAY_PRI_KEY_PATH = wxpayPriKeyPath;
    }

    // 微信商户序列号
    public static String WXPAY_MERCHANT_SERIAL_NUMBER;
    @Value("${pay.wxpay.merchant_serial_number}")
    public void setWxpayMerchantSerialNumber(String serialNumber) {
        WXPAY_MERCHANT_SERIAL_NUMBER = serialNumber;
    }

    public static void printInfo() {
        LOGGER.info("secure info::" + DB_API_KEY);
        LOGGER.info(DB_SECRET_KEY);
        LOGGER.info(IV_SEED);
        LOGGER.info(MAIL_SENDER_NAME);
        LOGGER.info(MAIL_SENDER);
        LOGGER.info(MAIL_PASSWORD);
        LOGGER.info(TENCENTCLOUD_SECRET_ID);
        LOGGER.info(TENCENTCLOUD_SECRET_KEY);
        LOGGER.info(ALIPAY_APP_PUB_KEY);
        LOGGER.info(ALIPAY_APP_PRI_KEY);
        LOGGER.info(ALIPAY_PUB_KEY);
        LOGGER.info(WXPAY_MERCHANT_ID);
        LOGGER.info(WXPAY_APIV3_KEY);
        LOGGER.info(WXPAY_PRI_KEY_PATH);
        LOGGER.info(WXPAY_MERCHANT_SERIAL_NUMBER);
    }

}
