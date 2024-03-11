package com.lineying.controller.api;

import cn.hutool.core.lang.Pair;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.common.AppCodeManager;
import com.lineying.common.ErrorCode;
import com.lineying.common.LoginType;
import com.lineying.controller.BaseController;
import com.lineying.controller.Checker;
import com.lineying.data.Column;
import com.lineying.entity.CommonSqlManager;
import com.lineying.service.ICommonService;
import com.lineying.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        List<Map<String, Object>> list = null;
        String appcode = jsonObject.get(Column.APPCODE).getAsString();
        String table = AppCodeManager.getUserTable(appcode);
        // username可以代表用户名、邮箱、apple token、wechat token
        String username = jsonObject.get(Column.USERNAME).getAsString();
        @LoginType
        int type = jsonObject.get(Column.TYPE).getAsInt();
        LOGGER.info("login type " + type);
        switch (type) {
            case LoginType.USERNAME:
            case LoginType.EMAIL:
                String password = jsonObject.get(Column.PASSWORD).getAsString();
                try {
                    if (type == LoginType.USERNAME) {
                        list = commonService.loginForUsername(CommonSqlManager.login(table, username, password));
                    } else {
                        list = commonService.loginForEmail(CommonSqlManager.login(table, username, password));
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
                    list = commonService.loginForUserId(CommonSqlManager.login(table, userInfo.getKey() + "", userInfo.getValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonCryptUtil.makeFail(e.getMessage());
                }
                break;
            case LoginType.APPLE: // 请求Apple公钥再验证太耗时了，直接查询
                String identityToken = jsonObject.get(Column.IDENTITY_TOKEN).getAsString();
                String clientId = jsonObject.get(Column.CLIENT_ID).getAsString();
                String brand = jsonObject.get(Column.BRAND).getAsString();
                String model = jsonObject.get(Column.MODEL).getAsString();
                //String appleUser = AppleUtil.login(clientId, identityToken);
                //if ("".equals(appleUser)) {
                //  return JsonCryptUtil.makeFail("apple verify fail");
                //}
                try {
                    list = commonService.loginForApple(CommonSqlManager.loginForAppleUser(table, username));
                    if (list.isEmpty()) {
                        String appleUser = username;
                        String genUsername = RandomUtil.makeName();
                        String ipaddr = IPUtil.getIpAddress(request);
                        try {
                            boolean result = addUser(table, genUsername,
                                    "", appleUser, brand, model, ipaddr);
                            if (result) {
                                list = commonService.loginForApple(CommonSqlManager.loginForAppleUser(table, username));
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
            int uid = (int) objUser.get(Column.ID);
            String pwd = (String) objUser.get(Column.PASSWORD);
            String token = TokenUtil.makeToken(uid, pwd);
            objUser.put(Column.TOKEN, token);
            objUser.remove(Column.PASSWORD);
            JsonObject obj = new JsonObject();
            obj.add(Column.DATA, new Gson().toJsonTree(list));
            return JsonCryptUtil.makeSuccess(obj);
        }
        return JsonCryptUtil.makeFail("unknown");
    }

    /**
     * 是否存在用户名
     * @param table
     * @param username
     * @return
     */
    private boolean hasUsername(String table, String username) {
        try {
            List<Map<String, Object>> list = commonService.list(CommonSqlManager.hasUsername(table, username));
            return !list.isEmpty();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否存在appleuser
     * @param table
     * @param appleuser
     * @return
     */
    private boolean hasAppleUser(String table, String appleuser) {
        try {
            List<Map<String, Object>> list = commonService.list(CommonSqlManager.hasAppleUser(table, appleuser));
            return !list.isEmpty();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 添加用户到数据库
     * @param table
     * @param username
     * @param password
     * @param appleUser
     * @param brand
     * @param model
     * @param ipaddr
     * @return
     */
    private boolean addUser(String table, String username, String password,
                            String appleUser, String brand, String model, String ipaddr) {
        long curTime = getCurrentTimeMs();
        try {
            return commonService.add(CommonSqlManager.addUser(table, username, password, appleUser, brand, model, ipaddr, curTime, curTime));
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    // 用户注册
    @RequestMapping("/register")
    public String register(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String appcode = jsonObject.get(Column.APPCODE).getAsString();
        String username = jsonObject.get(Column.USERNAME).getAsString();
        String password = jsonObject.get(Column.PASSWORD).getAsString();
        String brand = jsonObject.get(Column.BRAND).getAsString();
        String model = jsonObject.get(Column.MODEL).getAsString();
        String table = AppCodeManager.getUserTable(appcode);
        String ipaddr = IPUtil.getIpAddress(request);
        try {
            if (hasUsername(table, username)) {
                return JsonCryptUtil.makeFail("username exist", ErrorCode.USERNAME_EXIST);
            }
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
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String appcode = jsonObject.get(Column.APPCODE).getAsString();
        int type = 0;
        try {
            type = jsonObject.get(Column.TYPE).getAsInt();
        } catch (Exception e) { }
        String username = jsonObject.get(Column.USERNAME).getAsString();
        String password = "";
        String appleUser = "";
        if (type == 0) {
            password = jsonObject.get(Column.PASSWORD).getAsString();
        } else if (type == 1) {
            appleUser = jsonObject.get(Column.APPLE_USER).getAsString();
        }
        String table = AppCodeManager.getUserTable(appcode);
        boolean result = false;
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            if (type == 0) {
                list = commonService.list(CommonSqlManager.queryUser(table, username, password));
            } else if (type == 1) {
                list = commonService.list(CommonSqlManager.queryUserForAppleUser(table, username, appleUser));
            }
            if (list.size() > 0) {
                result = commonService.delete(CommonSqlManager.deleteUser(table, username));
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

        String appcode = request.getParameter(Column.APPCODE);
        String username = request.getParameter(Column.USERNAME);
        String password = request.getParameter(Column.PASSWORD);
        String table = AppCodeManager.getUserTable(appcode);
        boolean result = false;
        try {
            List<Map<String, Object>> list = commonService.list(CommonSqlManager.queryUser(table, username, password));
            if (list.size() > 0) {
                result = commonService.delete(CommonSqlManager.deleteUser(table, username));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            return "注销失败！";
        }
        return "注销成功！";
    }

}
