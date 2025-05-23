package com.lineying.common;

import com.lineying.factory.YamlPropertySourceFactory;
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

    // 微信支付私钥文件路径
    public static String WXPAY_PRI_KEY_PATH;
    @Value("${pay.wxpay.pri_key_path}")
    public void setWxpayPriKeyPath(String wxpayPriKeyPath) {
        WXPAY_PRI_KEY_PATH = wxpayPriKeyPath;
    }

    // 微信支付私钥内容
    public static String WXPAY_PRI_KEY;
    @Value("${pay.wxpay.pri_key}")
    public void setWxpayPriKey(String wxpayPriKey) {
        WXPAY_PRI_KEY = wxpayPriKey;
    }

    // 微信支付证书文件路径
    public static String WXPAY_CERT_PATH;
    @Value("${pay.wxpay.cert_path}")
    public void setWxpayCertPath(String wxpayCertPath) {
        WXPAY_CERT_PATH = wxpayCertPath;
    }

    // 微信商户序列号
    public static String WXPAY_MERCHANT_SERIAL_NUMBER;
    @Value("${pay.wxpay.merchant_serial_number}")
    public void setWxpayMerchantSerialNumber(String serialNumber) {
        WXPAY_MERCHANT_SERIAL_NUMBER = serialNumber;
    }

    // Apple API 集成 团队密钥issuer_id
    public static String APPLE_ISSUER_ID;
    @Value("${pay.apple.issuer_id}")
    public void setAppleIssuerId(String issuerId) {
        APPLE_ISSUER_ID = issuerId;
    }

    // Apple API 集成 团队密钥key_id
    public static String APPLE_KEY_ID;
    @Value("${pay.apple.key_id}")
    public void setAppleKeyId(String keyId) {
        APPLE_KEY_ID = keyId;
    }

    // Apple API 集成 团队密钥文件路径
    public static String APPLE_AUTH_KEY_PATH;
    @Value("${pay.apple.auth_key_path}")
    public void setAppleAuthKeyPath(String authKeyPath) {
        APPLE_AUTH_KEY_PATH = authKeyPath;
    }

    // Apple API 集成 根证书
    public static String APPLE_COMPUTER_ROOT_CERT_PATH;
    @Value("${pay.apple.computer_root_cert_path}")
    public void setAppleComputerRootCertPath(String computerRootCertPath) {
        APPLE_COMPUTER_ROOT_CERT_PATH = computerRootCertPath;
    }

    // Apple API 集成 根证书
    public static String APPLE_INC_ROOT_CERT_PATH;
    @Value("${pay.apple.inc_root_cert_path}")
    public void setAppleIncRootCertPath(String incRootCertPath) {
        APPLE_INC_ROOT_CERT_PATH = incRootCertPath;
    }

    // Apple API 集成 根证书
    public static String APPLE_ROOT_CA_G2;
    @Value("${pay.apple.root_ca_g2_path}")
    public void setAppleRootCaG2(String rootCaG2) {
        APPLE_ROOT_CA_G2 = rootCaG2;
    }

    // Apple API 集成 根证书
    public static String APPLE_ROOT_CA_G3;
    @Value("${pay.apple.root_ca_g3_path}")
    public void setAppleRootCaG3(String rootCaG3) {
        APPLE_ROOT_CA_G3 = rootCaG3;
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
        LOGGER.info(APPLE_ISSUER_ID);
        LOGGER.info(APPLE_KEY_ID);
        LOGGER.info(APPLE_AUTH_KEY_PATH);
        LOGGER.info(APPLE_COMPUTER_ROOT_CERT_PATH);
        LOGGER.info(APPLE_INC_ROOT_CERT_PATH);
        LOGGER.info(APPLE_ROOT_CA_G2);
        LOGGER.info(APPLE_ROOT_CA_G3);
    }

}
