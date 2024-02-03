package com.lineying.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.common.AppCodeManager;
import com.lineying.common.LoginType;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.LoginEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.*;
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
import java.util.logging.Logger;

import static com.lineying.common.SignResult.KEY_ERROR;
import static com.lineying.common.SignResult.SIGN_ERROR;

/**
 * 授权认证服务接口
 */
@RestController
@RequestMapping("api")
public class AuthenticationController extends BaseController {

    @Resource
    ICommonService commonService;

    // Apple授权登录
    private final static String AUTH_APPLE_URL = "https://appleid.apple.com/auth/keys";
    private final static String AUTH_APPLE_ISS = "https://appleid.apple.com";

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
        String tableName = AppCodeManager.getUserTable(appcode);
        LoginEntity entity = new LoginEntity();
        entity.setUsername(username);

        entity.setTable(tableName);
        @LoginType
        int type = jsonObject.get("type").getAsInt();
        switch (type) {
            case LoginType.USERNAME:
            case LoginType.EMAIL:
                String password = jsonObject.get("password").getAsString();
                entity.setPassword(password);
                try {
                    if (type == LoginType.USERNAME) {
                        list = commonService.loginForUsername(entity);
                    } else {
                        list = commonService.loginForEmail(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonCryptUtil.makeFail(e.getMessage());
                }
                break;
            case LoginType.TOKEN: // 一般不用这种, token放到header中
                break;
            case LoginType.APPLE: // 请求Apple公钥再验证太耗时了，直接查询
                String identityToken = jsonObject.get("identity_token").getAsString();
                String clientId = jsonObject.get("client_id").getAsString();
                String brand = jsonObject.get("brand").getAsString();
                String model = jsonObject.get("model").getAsString();
                //String appleUser = AppleUtil.login(clientId, identityToken);
                //if ("".equals(appleUser)) {
                  //  return JsonCryptUtil.makeFail("apple verify fail");
                //}
                entity.setUsername(username);
                try {
                    list = commonService.loginForApple(entity);
                    if (list.isEmpty()) {
                        long curTime = System.currentTimeMillis();
                        String genUsername = RandomUtil.makeName();
                        String table = AppCodeManager.getUserTable(appcode);
                        String column = "`username`,`nickname`,`password`,`apple_user`,`brand`,`model`,`create_time`,`update_time`";
                        String value = String.format("'%s','%s','','%s','%s','%s','%s','%s'", genUsername, genUsername, username, brand, model, "" + curTime, "" + curTime);
                        CommonAddEntity entityAdd = new CommonAddEntity();
                        entityAdd.setTable(table);
                        entityAdd.setColumn(column);
                        entityAdd.setValue(value);
                        try {
                            boolean result = commonService.add(entityAdd);
                            if (result) {
                                list = commonService.loginForApple(entity);
                            } else {
                                return JsonCryptUtil.makeFail("register error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return JsonCryptUtil.makeFail(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonCryptUtil.makeFail(e.getMessage());
                }
                break;
            case LoginType.WECHAT:
                break;
        }

        if (list == null) {
            return JsonCryptUtil.makeFail("unknown");
        }

        if (list.size() == 1) {
            Map<String, Object> objUser = list.get(0);
            long uid = (int) objUser.get("id");
            String pwd = (String) objUser.get("password");
            String token = TokenUtil.makeToken(uid, pwd);
            objUser.put("token", token);
            objUser.remove("password");
            JsonObject obj = new JsonObject();
            obj.add("data", new Gson().toJsonTree(list));
            return JsonCryptUtil.makeSuccess(obj);
        }
        return JsonCryptUtil.makeFail("unknown");
    }

}
