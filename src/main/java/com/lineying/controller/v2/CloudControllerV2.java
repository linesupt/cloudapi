package com.lineying.controller.v2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.bean.CloudData;
import com.lineying.common.AppCodeManager;
import com.lineying.controller.BaseController;
import com.lineying.controller.Checker;
import com.lineying.entity.CommonSqlManager;
import com.lineying.service.ICommonService;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.JsonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用级接口
 */
@RestController
@RequestMapping("v2")
public class CloudControllerV2 extends BaseController {

    @Resource
    ICommonService commonService;

    /**
     * 第三方云服务数据
     */
    private static Map<String, CloudData> mCloudData = new HashMap<>();

    /**
     * 获取内购商品列表
     * @param request
     * @return
     */
    @RequestMapping("/goodslist")
    public String getGoodsList(HttpServletRequest request) {

        Checker checker = doCheck(request);
        if (!checker.isValid()) {
            return checker.getResult();
        }
        JsonObject jsonObject = checker.getDataObject();
        String locale = checker.getLocale();
        String appcode = jsonObject.get("appcode").getAsString();
        String table = AppCodeManager.getGoodsTable(appcode);

        List<Map<String, Object>> list;
        try {
            list = commonService.list(CommonSqlManager.queryGoodsList(table, locale));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
        JsonObject obj = new JsonObject();
        obj.add("data", new Gson().toJsonTree(list));
        return JsonCryptUtil.makeSuccess(obj);
    }

    /**
     * 数据同步
     * @param request
     *  type 0: 添加数据 1：拉取下载数据
     * @return
     */
    @RequestMapping("/user/datasync")
    public String dataSync(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        int type = jsonObject.get("type").getAsInt();
        int uid = jsonObject.get("uid").getAsInt();
        String appcode = jsonObject.get("appcode").getAsString();
        String table = AppCodeManager.getUserSettingTable(appcode);
        if (type == 0) { // 查询用户设置
            List<Map<String, Object>> list;
            try {
                list = commonService.list(CommonSqlManager.queryUserSetting(table, uid));
            } catch (Exception e) {
                e.printStackTrace();
                return JsonCryptUtil.makeFail(e.getMessage());
            }
            JsonObject obj = new JsonObject();
            obj.add("data", new Gson().toJsonTree(list));
            return JsonCryptUtil.makeSuccess(obj);
        } else if (type == 1) { // 上传用户设置
            String settings = jsonObject.get("settings").getAsString();
            String data = jsonObject.get("data").getAsString();
            // 先判断用户是否有设置数据
            boolean hasData = querySetting(table, uid);
            if (!hasData) { // 没有数据、添加
                boolean result = false;
                try {
                    result = commonService.add(CommonSqlManager.addUserSetting(table, uid, settings, data));
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonCryptUtil.makeFail(e.getMessage());
                }
                return JsonCryptUtil.makeResult(result);
            }
            boolean result = false;
            try {
                result = commonService.update(CommonSqlManager.updateUserSetting(table, uid, settings, data));
            } catch (Exception e) {
                e.printStackTrace();
                return JsonCryptUtil.makeFail(e.getMessage());
            }

            return JsonCryptUtil.makeResult(result);
        }
        return JsonCryptUtil.makeFail("unknown");
    }

    /**
     * 是否有设置数据
     * @param table
     * @param uid
     * @return
     */
    private boolean querySetting(String table, int uid) {

        List<Map<String, Object>> list = null;
        try {
            list = commonService.list(CommonSqlManager.queryUserSetting(table, uid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }

    @RequestMapping("/cloud/select")
    public String cloudSelect(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String cate = jsonObject.get("cate").getAsString();
        CloudData bean = mCloudData.get(cate);
        if (bean == null) {
            return JsonUtil.makeFailNoData();
        }
        boolean enabled = bean.validate();
        if (!enabled) {
            return JsonUtil.makeFailDataExpired();
        }
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(bean.toData());
        JsonObject obj = new JsonObject();
        obj.add("data", new Gson().toJsonTree(list));
        return JsonCryptUtil.makeSuccess(obj);
    }

    /**
     * 上传数据
     * @param request
     * @return
     */
    @RequestMapping("/cloud/add")
    public String cloudAdd(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        int uid = jsonObject.get("uid").getAsInt();
        String cate = jsonObject.get("cate").getAsString();
        String text = jsonObject.get("text").getAsString();
        String model = jsonObject.get("model").getAsString();
        String ipaddr = jsonObject.get("ipaddr").getAsString();
        CloudData bean = new CloudData(uid, cate, text, model, ipaddr);
        mCloudData.put(cate, bean);

        return JsonCryptUtil.makeSuccess();
    }

    /**
     * 服务器时间戳（s）
     * @return
     */
    @RequestMapping("/timestamp")
    public long timestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 服务器时间戳（ms）
     * @return
     */
    @RequestMapping("/mstimestamp")
    public long timestampMillis() {
        return System.currentTimeMillis();
    }

}
