package com.lineying.util;


import cn.hutool.core.lang.Pair;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token生成器
 * jwt由header（头部），playload（载荷）、signature（签名三部分组成）
 */
public class TokenUtil {
    public static final long EXPIRED_INTERVAL = 30 * 24 * 3600_000L;
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
    public static String makeToken(int userId, String password) {
        long iat = System.currentTimeMillis();
        long exp = iat + EXPIRED_INTERVAL;
        Date expDate = new Date(exp);
        String encryptPwd = "";
        if (!"".equals(password)) {
            encryptPwd = AESUtil.encrypt(password);
        }
        //由于该生成器设置Header的参数为一个<String, Object>的Map
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "jwt");   //设置token的type为jwt
        headers.put("alg", "hs256");  //表明加密的算法为HS256
        // claim为自定义交换信息
        return JWT.create()
                .withHeader(headers)
                .withIssuer(ISS)
                .withExpiresAt(expDate)
                .withIssuedAt(new Date(iat))
                .withAudience("")
                .withSubject("")
                .withClaim("uid", userId)
                .withClaim("pwd", encryptPwd)
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    /**
     * 签名验证
     * @param token
     * @return
     */
    public static int verify(String token) {
        int userId = 0;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET))
                    .withIssuer(ISS).build();
            DecodedJWT jwt = verifier.verify(token);
            userId = jwt.getClaim("uid").asInt();
            if (userId <= 0) {
                return -2; // 无效用户
            }
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            return -4; // 过期
        } catch (InvalidClaimException e) {
            e.printStackTrace();
            return -3; // 发行者不对
        } catch (Exception e){
            e.printStackTrace();
            return -1; // 未知错误
        }
        return userId;
    }

    /**
     * 解析登录信息
     * @param token
     * @return
     */
    public static Pair<Integer, String> parse(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET))
                    .withIssuer(ISS).build();
            DecodedJWT jwt = verifier.verify(token);
            int userId = jwt.getClaim("uid").asInt();
            if (userId <= 0) {
                return null; // 无效用户
            }
            String signPwd = jwt.getClaim("pwd").asString();
            String password = AESUtil.decrypt(signPwd);
            return new Pair<>(userId, password);
        } catch (TokenExpiredException e) {
            e.printStackTrace();
        } catch (InvalidClaimException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
