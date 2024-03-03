package com.lineying.controller.v2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.common.AppCodeManager;
import com.lineying.controller.BaseController;
import com.lineying.controller.Checker;
import com.lineying.entity.CommonQueryEntity;
import com.lineying.entity.CommonUpdateEntity;
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
        String where = "id='" + id + "' and password='" + password + "'";
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        entity.setColumn("*");
        entity.setSort("desc");
        entity.setSortColumn("id");
        List<Map<String, Object>> list;
        try {
            list = commonService.list(entity);
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
        int id = jsonObject.get("id").getAsInt();
        String column = jsonObject.get("column").getAsString();
        String value = jsonObject.get("value").getAsString();

        String table = AppCodeManager.getUserTable(appcode);
        String where = "id='" + id + "' and column='" + value + "'";
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        entity.setColumn(column);
        entity.setSort("desc");
        entity.setSortColumn(column);
        List<Map<String, Object>> list;
        try {
            list = commonService.list(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        JsonObject obj = new JsonObject();
        obj.add("data", new Gson().toJsonTree(list));
        return JsonCryptUtil.makeSuccess(obj);
    }

    private boolean checkUser(int type, String table, String password, int uid, String email, String mobile) {
        // 先查询旧密码
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn("*");
        entity.setSort("desc");
        entity.setSortColumn("id");

        String where = "";
        switch (type) {
            case 0: // uid
                where = "id='" + uid + "' and password='" + password + "'";
                break;
            case 1: // email
                where = "email='" + email + "' and password='" + password + "'";
                break;
            case 2: // mobile
                where = "mobile='" + mobile + "' and password='" + password + "'";
                break;
        }
        entity.setWhere(where);

        List<Map<String, Object>> list = null;
        try {
            list = commonService.list(entity);
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
        String where = "";
        switch (type) {
            case 0: // uid
                uid = jsonObject.get("id").getAsInt();
                where = "id='" + uid + "'";
                break;
            case 1: // email
                email = jsonObject.get("email").getAsString();
                where = "email='" + email + "'";
                break;
            case 2: // mobile
                mobile = jsonObject.get("mobile").getAsString();
                where = "mobile='" + mobile + "'";
                break;
        }
        boolean hasUser = checkUser(type, table, passwordOld, uid, email, mobile);
        if (!hasUser) {
            return JsonCryptUtil.makeFail("old password error");
        }
        String set = "password='" + password + "'";
        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setSet(set);
        entity.setWhere(where);
        entity.setTable(table);
        boolean result = false;
        try {
            result = commonService.update(entity);
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
        String set = column + "='" + value + "'";

        String where = "id='" + uid + "'";
        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setSet(set);
        entity.setWhere(where);
        entity.setTable(table);
        boolean result = false;
        try {
            result = commonService.update(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }

        return JsonCryptUtil.makeResult(result);
    }


}
