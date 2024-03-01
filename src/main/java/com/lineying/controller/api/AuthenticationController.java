package com.lineying.controller.api;

import cn.hutool.core.lang.Pair;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.common.AppCodeManager;
import com.lineying.common.LoginType;
import com.lineying.controller.BaseController;
import com.lineying.controller.CheckPair;
import com.lineying.entity.CommonAddEntity;
import com.lineying.entity.CommonQueryEntity;
import com.lineying.entity.LoginEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
        CheckPair pair = checkValid(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        List<Map<String, Object>> list = null;
        String appcode = jsonObject.get("appcode").getAsString();
        String tableName = AppCodeManager.getUserTable(appcode);
        // username可以代表用户名、邮箱、apple token、wechat token
        String username = jsonObject.get("username").getAsString();
        LoginEntity entity = new LoginEntity();
        @LoginType
        int type = jsonObject.get("type").getAsInt();
        LOGGER.info("login type " + type);
        switch (type) {
            case LoginType.USERNAME:
            case LoginType.EMAIL:
                String password = jsonObject.get("password").getAsString();
                entity.setUsername(username);
                entity.setPassword(password);
                entity.setTable(tableName);
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
            case LoginType.TOKEN: // 重载用户数据时
                String token = username;
                if ("".equals(token)) {
                    return JsonCryptUtil.makeFail("token error");
                }
                int resultCode = TokenUtil.verify(token);
                if (resultCode != 0) {
                    return JsonCryptUtil.makeFail("token error " + resultCode);
                }
                Pair<Integer, String> userInfo = TokenUtil.parse(token);
                if (userInfo == null) {
                    return JsonCryptUtil.makeFail("token error uid, pwd null" + resultCode);
                }
                try {
                    LOGGER.info("token login::" + userInfo.getKey() + " - " + userInfo.getValue());
                    entity.setUsername(userInfo.getKey() + "");
                    entity.setPassword(userInfo.getValue());
                    entity.setTable(tableName);
                    list = commonService.loginForUserId(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonCryptUtil.makeFail(e.getMessage());
                }
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
                try {
                    entity.setUsername(username);
                    entity.setTable(tableName);
                    list = commonService.loginForApple(entity);
                    if (list.isEmpty()) {
                        String appleUser = username;
                        String genUsername = RandomUtil.makeName();
                        String ipaddr = IPUtil.getIpAddress(request);
                        try {
                            boolean result = addUser(tableName, genUsername,
                                    "", appleUser, brand, model, ipaddr);
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
            int uid = (int) objUser.get("id");
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

    /**
     * 添加用户到数据库
     * @param tableName
     * @param username
     * @param password
     * @param appleUser
     * @param brand
     * @param model
     * @param ipaddr
     * @return
     */
    private boolean addUser(String tableName, String username, String password,
                            String appleUser, String brand, String model, String ipaddr) {
        String curTime = getCurrentTimeMs() + "";
        String column = "";
        String value = "";
        if ("".equals(appleUser)) { // 空的时候由于字段唯一性不准保存
            column = "`username`,`nickname`,`password`,`brand`,`model`,`ipaddr`,`create_time`,`update_time`";
            value = String.format("'%s','%s','%s','%s','%s','%s','%s','%s'", username, username, password, brand, model, ipaddr, curTime, curTime);
        } else {
            column = "`username`,`nickname`,`password`,`apple_user`,`brand`,`model`,`ipaddr`,`create_time`,`update_time`";
            value = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s'", username, username, password, appleUser, brand, model, ipaddr, curTime, curTime);
        }
        CommonAddEntity entityAdd = new CommonAddEntity();
        entityAdd.setTable(tableName);
        entityAdd.setColumn(column);
        entityAdd.setValue(value);
        try {
            return commonService.add(entityAdd);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    // 用户注册
    @RequestMapping("/register")
    public String register(HttpServletRequest request) {
        CheckPair pair = checkValid(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String appcode = jsonObject.get("appcode").getAsString();
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        String brand = jsonObject.get("brand").getAsString();
        String model = jsonObject.get("model").getAsString();
        String table = AppCodeManager.getUserTable(appcode);
        String ipaddr = IPUtil.getIpAddress(request);
        try {
            boolean result = addUser(table, username, password, "", brand, model, ipaddr);
            if (!result) {
                return JsonCryptUtil.makeFail("register error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }

        return JsonCryptUtil.makeSuccess();
    }

    // 用户注销
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        CheckPair pair = checkValid(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String appcode = jsonObject.get("appcode").getAsString();
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();

        String table = AppCodeManager.getUserTable(appcode);
        String where = "username='" + username + "' and " + "password='" + password + "'";
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        boolean result = false;
        try {

            entity.setColumn("*");
            entity.setSort("desc");
            entity.setSortColumn("id");
            List<Map<String, Object>> list = commonService.list(entity);
            if (list.size() > 0) {
                String whereDel = "username='" + username + "'";
                entity.setWhere(whereDel);
                result = commonService.delete(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        return JsonCryptUtil.makeResult(result);
    }

    // 用户注销(网页端注销接口)
    @RequestMapping("/logoutweb")
    public String logoutweb(HttpServletRequest request) {

        String appcode = request.getParameter("appcode");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String table = AppCodeManager.getUserTable(appcode);
        String where = "username='" + username + "' and " + "password='" + password + "'";
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        boolean result = false;
        try {

            entity.setColumn("*");
            entity.setSort("desc");
            entity.setSortColumn("id");
            List<Map<String, Object>> list = commonService.list(entity);
            if (list.size() > 0) {
                //Map<String, Object> objectMap = list.get(0);
                //LOGGER.info("user::" + objectMap);
                String whereDel = "username='" + username + "'";
                entity.setWhere(whereDel);
                result = commonService.delete(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        return JsonCryptUtil.makeResult(result);
    }

}
