package com.lineying.controller.v2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.manager.TableManager;
import com.lineying.common.ErrorCode;
import com.lineying.controller.BaseController;
import com.lineying.controller.Checker;
import com.lineying.data.Column;
import com.lineying.data.Param;
import com.lineying.entity.CommonSqlManager;
import com.lineying.service.ICommonService;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.TokenUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 授权认证服务接口
 */
@RestController
@RequestMapping("v2")
public class AuthenticationControllerV2 extends BaseController {

    @Resource
    ICommonService commonService;

    /**
     * 验证密码
     * @param request
     * @return
     */
    @RequestMapping("/checkpwd")
    public String checkPassword(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String appcode = jsonObject.get(Column.APPCODE).getAsString();
        int id = jsonObject.get(Column.ID).getAsInt();
        String password = jsonObject.get(Column.PASSWORD).getAsString();
        String table = TableManager.getUserTable(appcode);
        List<Map<String, Object>> list;
        try {
            list = commonService.list(CommonSqlManager.queryPasswordForId(table, password, id));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        JsonObject obj = new JsonObject();
        obj.add(Param.Key.DATA, new Gson().toJsonTree(list));
        return JsonCryptUtil.makeSuccess(obj);
    }

    /**
     * 查询用户表字段
     * @param request
     * @return
     */
    @RequestMapping("/user/queryattr")
    public String queryAttr(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String appcode = jsonObject.get(Column.APPCODE).getAsString();
        int uid = jsonObject.get(Column.ID).getAsInt();
        String column = jsonObject.get(Column.COLUMN).getAsString();
        String value = jsonObject.get(Column.VALUE).getAsString();
        String table = TableManager.getUserTable(appcode);

        List<Map<String, Object>> list;
        try {
            list = commonService.list(CommonSqlManager.queryAttr(table, uid, column, value));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        JsonObject obj = new JsonObject();
        obj.add(Param.Key.DATA, new Gson().toJsonTree(list));
        return JsonCryptUtil.makeSuccess(obj);
    }

    /**
     * 是否存在用户
     * @param type
     * @param table
     * @param password
     * @param uid
     * @param email
     * @param mobile
     * @return
     */
    private boolean hasUser(int type, String table, String password, int uid, String email, String mobile, String appleuser) {
        List<Map<String, Object>> list = null;
        try {
            switch (type) {
                case 0: // uid
                    list = commonService.list(CommonSqlManager.queryPasswordForId(table, password, uid));
                    break;
                case 1: // email
                    list = commonService.list(CommonSqlManager.queryPasswordForEmail(table, password, email));
                    break;
                case 2: // mobile
                    list = commonService.list(CommonSqlManager.queryPasswordForMobile(table, password, mobile));
                    break;
                case 3: // appleuser
                    list = commonService.list(CommonSqlManager.queryUserForAppleUser(table, appleuser));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 更新密码
     * @param request
     * @return
     */
    @RequestMapping("/user/updatepwd")
    public String updatePwd(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String appcode = jsonObject.get(Column.APPCODE).getAsString();
        int type = jsonObject.get(Column.TYPE).getAsInt();
        String password = jsonObject.get(Column.PASSWORD).getAsString();
        String table = TableManager.getUserTable(appcode);

        int uid = 0;
        String email = "";
        String mobile = "";
        String appleuser = "";
        String passwordOld = "";
        switch (type) {
            case 0: // uid
            case 1: // email
            case 2: // mobile
                passwordOld = jsonObject.get(Column.PASSWORD_OLD).getAsString();
                switch (type) {
                    case 0:
                        uid = jsonObject.get(Column.ID).getAsInt();
                        break;
                    case 1:
                        email = jsonObject.get(Column.EMAIL).getAsString();
                        break;
                    case 2:
                        mobile = jsonObject.get(Column.MOBILE).getAsString();
                        break;
                }
                break;
            case 3: // appleuser
                appleuser = jsonObject.get(Column.APPLE_USER).getAsString();
                break;
        }
        boolean hasUser = hasUser(type, table, passwordOld, uid, email, mobile, appleuser);
        if (!hasUser) {
            return JsonCryptUtil.makeFail("old password error");
        }
        boolean result = false;
        try {
            switch (type) {
                case 0: // uid
                    result = commonService.update(CommonSqlManager.updatePasswordForUid(table, password, uid));
                    break;
                case 1: // email
                    List<Map<String, Object>> userList = commonService.list(CommonSqlManager.queryUserForEmail(table, email, password));
                    if (userList.size() > 0) {
                        uid = (Integer) userList.get(0).get(Column.ID);
                    }
                    result = commonService.update(CommonSqlManager.updatePasswordForEmail(table, password, email));
                    break;
                case 2: // mobile
                    List<Map<String, Object>> userMobileList = commonService.list(CommonSqlManager.queryUserForMobile(table, mobile, password));
                    if (userMobileList.size() > 0) {
                        uid = (Integer) userMobileList.get(0).get(Column.ID);
                    }
                    result = commonService.update(CommonSqlManager.updatePasswordForMobile(table, password, mobile));
                    break;
                case 3: // apple user
                    List<Map<String, Object>> userAppleList = commonService.list(CommonSqlManager.queryUserForAppleUser(table, appleuser));
                    if (userAppleList.size() > 0) {
                        LOGGER.info("user data ===>>" + userAppleList.get(0));
                        uid = (Integer) userAppleList.get(0).get(Column.ID);
                    }
                    result = commonService.update(CommonSqlManager.updatePasswordForAppleUser(table, password, appleuser));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        LOGGER.info("uid===>>" + uid);
        String token = "";
        if (uid > 0) {
            token = TokenUtil.makeToken(uid, password);
        }
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty(Column.TOKEN, token);
        return JsonCryptUtil.makeSuccess(resultObj);
    }

    /**
     * 更新用户表字段（限制一个字段）
     * @param request
     * @return
     */
    @RequestMapping("/user/updateattr")
    public String updateAttr(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String appcode = jsonObject.get(Column.APPCODE).getAsString();
        String column = jsonObject.get(Column.COLUMN).getAsString();
        String value = jsonObject.get(Column.VALUE).getAsString();
        int uid = jsonObject.get(Column.ID).getAsInt();
        String table = TableManager.getUserTable(appcode);

        if (Objects.equals(Column.USERNAME, column)) {
            if (hasUsername(table, column)) {
                return JsonCryptUtil.makeFail("username exist", ErrorCode.USERNAME_EXIST);
            }
        } else if (Objects.equals(Column.EMAIL, column)) {
            if (hasEmail(table, column)) {
                return JsonCryptUtil.makeFail("email exist", ErrorCode.EMAIL_EXIST);
            }
        } else if (Objects.equals(Column.MOBILE, column)) {
            if (hasMobile(table, column)) {
                return JsonCryptUtil.makeFail("mobile exist", ErrorCode.MOBILE_EXIST);
            }
        } else if (Objects.equals(Column.APPLE_USER, column)) {
            if (!"".equals(value)) {
                if (hasAppleUser(table, column)) {
                    return JsonCryptUtil.makeFail("appleUser exist", ErrorCode.APPLE_USER_EXIST);
                }
            } else { // 解除绑定

            }
        }
        boolean result = false;
        try {
            result = commonService.update(CommonSqlManager.updateAttr(table, uid, column, value));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }

        return JsonCryptUtil.makeResult(result);
    }

    /**
     * 判断用户名是否存在
     * @param table
     * @param username
     * @return
     */
    private boolean hasUsername(String table, String username) {
        try {
            return !commonService.list(CommonSqlManager.queryUsername(table, username)).isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断邮箱是否存在
     * @param table
     * @param email
     * @return
     */
    private boolean hasEmail(String table, String email) {
        try {
            return !commonService.list(CommonSqlManager.queryEmail(table, email)).isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断手机号是否存在
     * @param table
     * @param mobile
     * @return
     */
    private boolean hasMobile(String table, String mobile) {
        try {
            return !commonService.list(CommonSqlManager.queryMobile(table, mobile)).isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断Appleuser是否存在
     * @param table
     * @param appleUser
     * @return
     */
    private boolean hasAppleUser(String table, String appleUser) {
        if ("NULL".equals(appleUser)) {
            return false;
        }
        try {
            return !commonService.list(CommonSqlManager.queryAppleUser(table, appleUser)).isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
