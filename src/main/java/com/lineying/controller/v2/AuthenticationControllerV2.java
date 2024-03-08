package com.lineying.controller.v2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.common.AppCodeManager;
import com.lineying.controller.BaseController;
import com.lineying.controller.Checker;
import com.lineying.entity.CommonSqlManager;
import com.lineying.service.ICommonService;
import com.lineying.util.JsonCryptUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
        String appcode = jsonObject.get("appcode").getAsString();
        int id = jsonObject.get("id").getAsInt();
        String password = jsonObject.get("password").getAsString();
        String table = AppCodeManager.getUserTable(appcode);
        List<Map<String, Object>> list;
        try {
            list = commonService.list(CommonSqlManager.queryPasswordForId(table, password, id));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        JsonObject obj = new JsonObject();
        obj.add("data", new Gson().toJsonTree(list));
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
        String appcode = jsonObject.get("appcode").getAsString();
        int uid = jsonObject.get("id").getAsInt();
        String column = jsonObject.get("column").getAsString();
        String value = jsonObject.get("value").getAsString();
        String table = AppCodeManager.getUserTable(appcode);

        List<Map<String, Object>> list;
        try {
            list = commonService.list(CommonSqlManager.queryAttr(table, uid, column, value));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        JsonObject obj = new JsonObject();
        obj.add("data", new Gson().toJsonTree(list));
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
    private boolean hasUser(int type, String table, String password, int uid, String email, String mobile) {
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
        String appcode = jsonObject.get("appcode").getAsString();
        int type = jsonObject.get("type").getAsInt();
        String password = jsonObject.get("password").getAsString();
        String passwordOld = jsonObject.get("password_old").getAsString();
        String table = AppCodeManager.getUserTable(appcode);

        int uid = 0;
        String email = "";
        String mobile = "";
        switch (type) {
            case 0: // uid
                uid = jsonObject.get("id").getAsInt();
                break;
            case 1: // email
                email = jsonObject.get("email").getAsString();
                break;
            case 2: // mobile
                mobile = jsonObject.get("mobile").getAsString();
                break;
        }
        boolean hasUser = hasUser(type, table, passwordOld, uid, email, mobile);
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
                    result = commonService.update(CommonSqlManager.updatePasswordForEmail(table, password, email));
                    break;
                case 2: // mobile
                    result = commonService.update(CommonSqlManager.updatePasswordForMobile(table, password, mobile));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }

        return JsonCryptUtil.makeResult(result);
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
        String appcode = jsonObject.get("appcode").getAsString();
        String column = jsonObject.get("column").getAsString();
        String value = jsonObject.get("value").getAsString();
        int uid = jsonObject.get("id").getAsInt();
        String table = AppCodeManager.getUserTable(appcode);

        boolean result = false;
        try {
            result = commonService.update(CommonSqlManager.updateAttr(table, uid, column, value));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }

        return JsonCryptUtil.makeResult(result);
    }


}
