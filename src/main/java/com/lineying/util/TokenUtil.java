package com.lineying.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lineying.common.SecureConfig;

import java.util.Date;
import java.util.Objects;

/**
 * token生成器
 * jwt由header（头部），playload（载荷）、signature（签名三部分组成）
 */
public class TokenUtil {
    public static final long EXPIRED_INTERVAL = 30 * 24 * 60 * 60 * 1000;
    // token秘钥
    public static final String TOKEN_SECRET = "d4c59fc79d";
    // 发行者
    public static final String ISS = "lineying";

    /**
     * 生成token
     * @param userId
     * @param password
     * @return
     */
    public static String makeToken(long userId, String password) {
        long iat = System.currentTimeMillis();
        long exp = iat + EXPIRED_INTERVAL;
        Date expDate = new Date(exp);
        String encryptPwd = "";
        if (!"".equals(password)) {
            encryptPwd = AESUtil.encrypt(SecureConfig.DB_SECRET_KEY, SecureConfig.IV_SEED, password);
        }
        // claim为自定义交换信息
        return JWT.create()
                .withIssuer(ISS)
                .withExpiresAt(expDate)
                .withAudience("")
                .withSubject("")
                .withClaim("uid", userId)
                .withClaim("pwd", encryptPwd)
                .withClaim("iat", iat) // 签发时间
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    /**
     * 签名验证
     * @param token
     * @return
     */
    public static int verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            long exp = jwt.getExpiresAt().getTime();
            long currentTime = System.currentTimeMillis();
            if (exp < currentTime) {
                return 1; // 过期
            }
            long userId = jwt.getClaim("uid").asLong();
            if (userId <= 0) {
                return 2; // 无效用户
            }
            String iss = jwt.getIssuer();
            if (!Objects.equals(ISS, iss)) {
                return 3; // 发行者不对
            }
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

}
