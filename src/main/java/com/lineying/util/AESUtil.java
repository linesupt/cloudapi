package com.lineying.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 *
 * @author ACGkaka
 * @since 2021-06-18 19:11:03
 */
public class AESUtil {
    /**
     * 日志相关
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);
    /**
     * 编码
     */
    private static final String ENCODING = "UTF-8";
    /**
     * 算法定义
     */
    private static final String HASH_ALGORITHM = "AES";

    /**
     * 指定填充方式
     */
    private static final String CIPHER_PADDING = "AES/ECB/PKCS5Padding";
    private static final String CIPHER_CBC_PADDING = "AES/CBC/PKCS5Padding";
    /**
     * 偏移量(CBC中使用，增强加密算法强度)
     */
    private static final String IV_SEED = "1234567812345678";

    /**
     * AES加密
     *
     * @param content 待加密内容
     * @param secret  密码
     * @return
     */
    public static String encrypt(String content, String secret) {
        if (content == null || content.isEmpty()) {
            LOGGER.info("AES encrypt: the content is null!");
            return null;
        }
        //判断秘钥是否为16位
        if (secret == null || secret.length() != 16) {
            LOGGER.info("AES encrypt: the secret is null or error!");
            return null;
        }
        try {
            //对密码进行编码
            byte[] bytes = secret.getBytes(ENCODING);
            //设置加密算法，生成秘钥
            SecretKeySpec skeySpec = new SecretKeySpec(bytes, HASH_ALGORITHM);
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
            //选择加密
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            //根据待加密内容生成字节数组
            byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
            //返回base64字符串
            return Base64Utils.encodeToString(encrypted);
        } catch (Exception e) {
            LOGGER.info("AES encrypt exception:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param secret  密码
     * @return
     */
    public static String decrypt(String content, String secret) {
        if (content == null || content.isEmpty()) {
            LOGGER.info("AES decrypt: the content is null!");
            return null;
        }
        //判断秘钥是否为16位
        if (secret == null || secret.length() != 16) {
            LOGGER.info("AES decrypt: the secret is null or error!");
            return null;
        }
        try {
            //对密码进行编码
            byte[] bytes = secret.getBytes(ENCODING);
            //设置解密算法，生成秘钥
            SecretKeySpec skeySpec = new SecretKeySpec(bytes, HASH_ALGORITHM);
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
            //选择解密
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            //先进行Base64解码
            byte[] decodeBase64 = Base64Utils.decodeFromString(content);

            //根据待解密内容进行解密
            byte[] decrypted = cipher.doFinal(decodeBase64);
            //将字节数组转成字符串
            return new String(decrypted, ENCODING);
        } catch (Exception e) {
            LOGGER.info("AES decrypt exception:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * AES_CBC加密
     *
     * @param content 待加密内容
     * @param secret  密码
     * @return
     */
    public static String encryptCBC(String content, String secret) {
        if (content == null || content.isEmpty()) {
            LOGGER.info("AES_CBC encrypt: the content is null!");
            return null;
        }
        if (secret == null || secret.length() != 16) {
            LOGGER.info("AES_CBC encrypt: the secret is null or error!");
            return null;
        }
        try {
            //对密码进行编码
            byte[] bytes = secret.getBytes(ENCODING);
            //设置加密算法，生成秘钥
            SecretKeySpec skeySpec = new SecretKeySpec(bytes, HASH_ALGORITHM);
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
            //偏移
            IvParameterSpec iv = new IvParameterSpec(IV_SEED.getBytes(ENCODING));
            //选择加密
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            //根据待加密内容生成字节数组
            byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
            //返回base64字符串
            return Base64Utils.encodeToString(encrypted);
        } catch (Exception e) {
            LOGGER.info("AES_CBC encrypt exception:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * AES_CBC解密
     *
     * @param content 待解密内容
     * @param secret  密码
     * @return
     */
    public static String decryptCBC(String content, String secret) {
        if (content == null || content.isEmpty()) {
            LOGGER.info("AES_CBC decrypt: the content is null!");
            return null;
        }
        if (secret == null || secret.length() != 16) {
            LOGGER.info("AES_CBC decrypt: the secret is null or error!");
            return null;
        }
        try {
            //对密码进行编码
            byte[] bytes = secret.getBytes(ENCODING);
            //设置解密算法，生成秘钥
            SecretKeySpec skeySpec = new SecretKeySpec(bytes, HASH_ALGORITHM);
            //偏移
            IvParameterSpec iv = new IvParameterSpec(IV_SEED.getBytes(ENCODING));
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
            //选择解密
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            //先进行Base64解码
            byte[] decodeBase64 = Base64Utils.decodeFromString(content);

            //根据待解密内容进行解密
            byte[] decrypted = cipher.doFinal(decodeBase64);
            //将字节数组转成字符串
            return new String(decrypted, ENCODING);
        } catch (Exception e) {
            LOGGER.info("AES_CBC decrypt exception:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // AES支持三种长度的密钥：128位、192位、256位。
        // 代码中这种就是128位的加密密钥，16字节 * 8位/字节 = 128位。
        String random = "abcdyz1234567890";
        System.out.println("随机key:" + random);
        System.out.println();

        System.out.println("---------加密---------");
        String aesResult = encrypt("测试AES加密12", random);
        System.out.println("aes加密结果:" + aesResult);
        System.out.println();

        System.out.println("---------解密---------");
        String decrypt = decrypt(aesResult, random);
        System.out.println("aes解密结果:" + decrypt);
        System.out.println();


        System.out.println("--------AES_CBC加密解密---------");
        String cbcResult = encryptCBC("测试AES加密12456", random);
        System.out.println("aes_cbc加密结果:" + cbcResult);
        System.out.println();

        System.out.println("---------解密CBC---------");
        String cbcDecrypt = decryptCBC(cbcResult, random);
        System.out.println("aes解密结果:" + cbcDecrypt);
        System.out.println();
    }
}
