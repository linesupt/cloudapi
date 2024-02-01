package com.lineying.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import io.jsonwebtoken.*;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import cn.hutool.core.codec.Base64;

/**
 * Apple登录工具
 */
public class AppleUtil {

    /**
     * 执行登录
     * @param clientId
     * @param identityToken
     * @return 返回用户唯一码，失败返回空
     */
    public static String login(String clientId, String identityToken) {
        // 解析id_token
        Map<String, JSONObject> json = parserIdentityToken(identityToken);
        JSONObject header = json.get("header");
        String kid = header.getStr("kid");
        // 根据kid，获取相应的公钥
        PublicKey publicKey = getPublicKey(kid);
        if (publicKey == null) {
            Logger.getGlobal().info("[苹果登录失败 publicKey获取失败 clientId<" + clientId + "> kid<" + kid + ">]");
            return "";
        }
        return getAppleUserId(clientId, publicKey, identityToken);
    }

    /**
     * 获取Apple user ID
     * @param appleClientId
     * @param publicKey
     * @param identityToken
     * @return
     */
    private static String getAppleUserId(String appleClientId, PublicKey publicKey, String identityToken) {
        String appleIssuerUrl = "https://appleid.apple.com";
        JwtParser jwtParser = Jwts.parser().requireIssuer(appleIssuerUrl).requireAudience(appleClientId).setSigningKey(publicKey);
        Jws<Claims> claim = jwtParser.parseClaimsJws(identityToken);
        if (claim != null) {
            Map<String, Object> result = claim.getBody();
            String iss = result.get("iss").toString();
            String sub = result.get("sub").toString();
            String aud = result.get("aud").toString();
            Long exp = Long.parseLong(result.get("exp").toString()) * 1000;
            Logger.getGlobal().info("[苹果登录iss<" + iss + "> sub<" + sub + "> aud<" + aud + "> exp<" + + exp + ">]");
            if (appleIssuerUrl.equals(iss) && appleClientId.equals(aud)) {
                Logger.getGlobal().info("[苹果账号登录成功 appleClientId<" + appleClientId + "> userId<" + sub + ">]");
                return sub;
            }
        }
        return "";
    }

    /**
     * 解析id_token
     *
     * @param identityToken
     * @return
     */
    private static Map<String, JSONObject> parserIdentityToken(String identityToken) {
        Map<String, JSONObject> map = new HashMap<>(2);
        String[] arr = identityToken.split("\\.");
        String deHeader = Base64.decodeStr(arr[0]);
        JSONObject header = JSONUtil.parseObj(deHeader);
        String dePayload = Base64.decodeStr(arr[1]);
        JSONObject payload = JSONUtil.parseObj(dePayload);
        map.put("header", header);
        map.put("payload", payload);
        return map;
    }

    /**
     * 获取苹果的公钥
     *
     * @param kid
     * @return
     */
    private static PublicKey getPublicKey(String kid) {
        Map<String, Map<String, String>> map = getAppleKeys();
        try {
            String n = map.get(kid).get("n");
            String e = map.get(kid).get("e");
            BigInteger modulus = new BigInteger(1, Base64.decode(n));
            BigInteger publicExponent = new BigInteger(1, Base64.decode(e));
            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
            //目前kty均为 "RSA"
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception e) {
            System.err.println("getPublicKey error:{}" + e);
        }
        return null;
    }

    /**
     * 请求苹果公钥信息(每次获取都不一样)
     *
     * @return
     */
    private static Map<String, Map<String, String>> getAppleKeys() {
        Map<String, Map<String, String>> result = new HashMap<>(2);
        String keys = HttpUtil.get("https://appleid.apple.com/auth/keys");
        JSONObject keysObject = JSONUtil.parseObj(keys);
        JSONArray jsonArray = keysObject.getJSONArray("keys");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Map<String, String> m = new HashMap<>();
            m.put("n", obj.getStr("n"));
            m.put("e", obj.getStr("e"));
            result.put(obj.getStr("kid"), m);
        }
        return result;
    }

}