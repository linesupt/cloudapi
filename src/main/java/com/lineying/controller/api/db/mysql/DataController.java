package com.lineying.controller.api.db.mysql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
 * 数据级接口控制
 */
@RestController
@RequestMapping("api/db/mysql")
public class DataController extends BaseController {

    @Resource
    ICommonService commonService;

    @RequestMapping("/select")
    public String select(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();

        String table = jsonObject.get("table").getAsString();
        String column = jsonObject.get("column").getAsString();
        String where = jsonObject.get("where").getAsString();
        String sort = jsonObject.get("sort").getAsString();
        String sortColumn = jsonObject.get("sort_column").getAsString();

        List<Map<String, Object>> list;
        try {
            list = commonService.list(CommonSqlManager.select(table, column, where, sort, sortColumn));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        JsonObject obj = new JsonObject();
        obj.add("data", new Gson().toJsonTree(list));
        return JsonCryptUtil.makeSuccess(obj);
    }

    @RequestMapping("/insert")
    public String insert(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();

        String table = jsonObject.get("table").getAsString();
        String column = jsonObject.get("column").getAsString();
        String value = jsonObject.get("value").getAsString();
        boolean result = false;
        try {
            result = commonService.add(CommonSqlManager.addColumnData(table, column, value));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        return JsonCryptUtil.makeResult(result);
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();

        String table = jsonObject.get("table").getAsString();
        String where = jsonObject.get("where").getAsString();
        boolean result = false;
        try {
            result = commonService.delete(CommonSqlManager.delete(table, where));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        return JsonCryptUtil.makeResult(result);
    }

    @RequestMapping("/update")
    public String update(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();

        String table = jsonObject.get("table").getAsString();
        String set = jsonObject.get("set").getAsString();
        String where = jsonObject.get("where").getAsString();
        boolean result = false;
        try {
            result = commonService.update(CommonSqlManager.update(table, set, where));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }

        return JsonCryptUtil.makeResult(result);
    }

    @RequestMapping("/command")
    public String command(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String sql = jsonObject.get("sql").getAsString();
        boolean result = false;
        try {
            result = commonService.command(CommonSqlManager.command(sql));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        return JsonCryptUtil.makeResult(result);
    }

}