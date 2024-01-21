package com.lineying.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.common.AppCodeManager;
import com.lineying.common.LoginType;
import com.lineying.entity.LoginEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.AESUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.SignUtil;
import io.jsonwebtoken.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lineying.common.SignResult.KEY_ERROR;
import static com.lineying.common.SignResult.SIGN_ERROR;

/**
 * Apple授权服务接口
 */
@RestController
@RequestMapping("api")
public class AppleServiceController extends BaseController {

    @Resource
    ICommonService commonService;

    // Apple授权登录
    private final static String authAppleUrl = "https://appleid.apple.com/auth/keys";
    private final static String authAppleIss = "https://appleid.apple.com";

    @RequestMapping("/login")
    public String login(HttpServletRequest request) {

        String key = request.getParameter("key");
        String secretData = request.getParameter("data");
        String signature = request.getParameter("signature");
        int signResult = SignUtil.validateSign(key, secretData, signature);
        switch (signResult) {
            case KEY_ERROR:
                return JsonCryptUtil.makeFailKey();
            case SIGN_ERROR:
                return JsonCryptUtil.makeFailSign();
        }

        String data = AESUtil.decrypt(secretData);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        if (!checkRequest(timestamp)) {
            return JsonCryptUtil.makeFailTime();
        }

        LOGGER.info("执行查询 " + key + " - " + data + " - " + signature);

        List<Map<String, Object>> list = null;
        String appcode = jsonObject.get("appcode").getAsString();
        // username可以代表用户名、邮箱、apple token、wechat token
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        String tableName = AppCodeManager.getUserTable(appcode);
        LoginEntity entity = new LoginEntity();
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setTable(tableName);
        @LoginType
        int type = jsonObject.get("type").getAsInt();
        switch (type) {
            case LoginType.USERNAME:
                try {
                    list = commonService.loginForUsername(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonCryptUtil.makeFail(e.getMessage());
                }
                break;
            case LoginType.EMAIL:
                try {
                    list = commonService.loginForEmail(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonCryptUtil.makeFail(e.getMessage());
                }
            case LoginType.TOKEN:
                // TODO 解析token数据内容
                break;
            case LoginType.APPLE:
                break;
            case LoginType.WECHAT:
                break;

        }


        if (list != null) {
            JsonObject obj = new JsonObject();
            obj.add("data", new Gson().toJsonTree(list));
            return JsonCryptUtil.makeSuccess(obj);
        }
        return JsonCryptUtil.makeFail("unknown");
    }


    public String doAppleLogin(String identityToken) {
        String[] arr = identityToken.split("\\.");
        String firstDate = new String(Base64.getDecoder().decode(arr[0]), StandardCharsets.UTF_8);
        String claim = new String(Base64.getDecoder().decode(arr[1]), StandardCharsets.UTF_8);

        // 开发者帐户中获取的 10 个字符的标识符密钥
        String kid = JsonParser.parseString(firstDate).getAsJsonObject().get("kid").getAsString();
        JsonObject claimObj = JsonParser.parseString(claim).getAsJsonObject();
        String aud = claimObj.get("aud").getAsString();
        String sub = claimObj.get("sub").getAsString();

        PublicKey publicKey = this.getPublicKey(kid);
        if (Objects.isNull(publicKey)) {
            throw new RuntimeException("apple授权登录的数据异常");
        }

        boolean reuslt = this.verifyAppleLoginCode(publicKey, identityToken, aud, sub);

        if (reuslt) {
        }
        return null;
    }

    /**
     * 验证Apple登录
     * @param publicKey
     * @param identityToken
     * @param aud
     * @param sub
     * @return
     */
    private boolean verifyAppleLoginCode(PublicKey publicKey, String identityToken, String aud, String sub) {
        boolean result = false;
        JwtParser jwtParser = Jwts.parser().setSigningKey(publicKey);
        jwtParser.requireIssuer(authAppleIss);
        jwtParser.requireAudience(aud);
        jwtParser.requireSubject(sub);
        try {
            Jws<Claims> claim = jwtParser.parseClaimsJws(identityToken);
            if (claim != null && claim.getBody().containsKey("auth_time")) {
                result = true;
            }
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("apple登录授权identityToken过期." + e.getLocalizedMessage());
        } catch (SignatureException e) {
            throw new RuntimeException("apple登录授权identityToken非法." + e.getLocalizedMessage());
        }
        return result;
    }

    private PublicKey getPublicKey(String kid) {
        /*
        try {
            CloseableHttpClient httpclient = HttpClientBuilder.create().build();
            URIBuilder uriBuilder = new URIBuilder(appleAuthUrl);
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse response = httpclient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity, "UTF-8");
            if (statusCode != HttpStatus.SC_OK) { // code = 200
                throw new RuntimeException(String.format("接口请求失败，url[%s], result[%s]", appleAuthUrl, result));
            }

            // 请求成功
            JSONObject content = JSONObject.parseObject(result);
            String keys = content.getString("keys");
            JSONArray keysArray = JSONObject.parseArray(keys);
            if (keysArray.isEmpty()) {
                return null;
            }

            for (Object key : keysArray) {
                JSONObject keyJsonObject = (JSONObject)key;
                if (kid.equals(keyJsonObject.getString("kid"))) {
                    String n = keyJsonObject.getString("n");
                    String e = keyJsonObject.getString("e");
                    BigInteger modulus = new BigInteger(1, Base64.decodeBase64(n));
                    BigInteger publicExponent = new BigInteger(1, Base64.decodeBase64(e));
                    RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    return kf.generatePublic(spec);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("获取PublicKey异常.", ex);
        }
         */
        return null;
    }

}
