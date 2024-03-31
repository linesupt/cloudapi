package com.lineying.controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.bean.AdntBrand;
import com.lineying.bean.CloudData;
import com.lineying.common.Config;
import com.lineying.common.TableManager;
import com.lineying.controller.BaseController;
import com.lineying.controller.Checker;
import com.lineying.data.Column;
import com.lineying.data.Param;
import com.lineying.entity.CommonSqlManager;
import com.lineying.manager.AdMediaManager;
import com.lineying.service.ICommonService;
import com.lineying.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 应用级接口
 */
@RestController
@RequestMapping("api")
public class CloudController extends BaseController {

    @Resource
    ICommonService commonService;

    /**
     * 更新配置
     * @param request
     * @return
     */
    @RequestMapping("/updateconf")
    public String updateConf(HttpServletRequest request) {
        Checker checker = doCheck(request);
        if (!checker.isValid()) {
            return checker.getResult();
        }
        try {
            // 配置广告显示规则
            List<Map<String, Object>> defMediaList = commonService.list(CommonSqlManager.queryDefaultMedia());
            if (defMediaList.size() > 0) {
                String defMedia = (String) defMediaList.get(0).get(Column.TYPE);
                Config.defMedia = defMedia;
            }
            List<Map<String, Object>> mediaPlanList = commonService.list(CommonSqlManager.queryMediaPlan());
            Config.mediaPlanList = mediaPlanList;

            Config.adntBrandList.clear();
            List<Map<String, Object>> adBrandList = commonService.list(CommonSqlManager.queryAdBrand(0));
            for (Map<String, Object> map : adBrandList) {
                String appcode = (String) map.get("appcode");
                String platform = (String) map.get("platform");
                String brand = ((String) map.get("brand")).toLowerCase();
                int status = (Integer) map.get("status");
                AdntBrand model = new AdntBrand(appcode, platform, brand, status);
                Config.adntBrandList.add(model);
            }

            LOGGER.info("config::" + Config.defMedia + "\n" + Config.mediaPlanList + "\n" + Config.adntBrandList);
            return JsonCryptUtil.makeSuccess();
        } catch (Exception e){
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
    }

    /**
     * 检查默认配置加载
     * @param request
     */
    private void checkMediaConfig(HttpServletRequest request) {
        if (Objects.equals(Config.defMedia, "")) {
            updateConf(request);
        }
    }

    /**
     * 检查是否禁用广告
     * @param appcode
     * @param platform
     * @param brand
     * @return
     */
    private boolean checkAdnt(String appcode, String platform, String brand) {
        try {
            for (AdntBrand model : Config.adntBrandList) {
                if (Objects.equals(appcode, model.getAppcode())
                        && Objects.equals(platform, model.getPlatform())
                        && Objects.equals(brand, model.getBrand())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 应用配置
     * @param request
     * @return
     */
    @RequestMapping("/appconf")
    public String appConf(HttpServletRequest request) {
        Checker checker = doCheck(request);
        if (!checker.isValid()) {
            return checker.getResult();
        }

        checkMediaConfig(request);
        LOGGER.info("device::" + checker);
        try {
            String platform = checker.getPlatform();
            String appcode = checker.getAppcode();
            JsonObject jsonObject = checker.getDataObject();
            String pkgname = jsonObject.get(Column.PKGNAME).getAsString();

            // 配置广告显示规则
            Map<String, Object> confMap = new HashMap<>();
            String brand = checker.getBrand() == null ? "" : checker.getBrand().toLowerCase();
            if (checkAdnt(appcode, platform, brand)) {
                List<Map<String, Object>> adList = AdMediaManager.makeEmptyAdList();
                LOGGER.info("adlist::" + adList);
                confMap.put(Param.Key.ADLIST, adList);
            } else {
                List<Map<String, Object>> adList = commonService.list(CommonSqlManager.queryAdList(appcode, platform, pkgname));
                confMap.put(Param.Key.ADLIST, adList);
            }
            confMap.put(Param.Key.ADDEF, Config.defMedia);
            confMap.put(Param.Key.MEDIA_PLAN, Config.mediaPlanList);
            LOGGER.info("adlistmap::" + confMap);
            return JsonCryptUtil.makeSuccess(confMap);
        } catch (Exception e){
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
    }


    /**
     * 获取最新版本信息
     * @param request
     * @return
     */
    @RequestMapping("/appversion")
    public String appversion(HttpServletRequest request) {
        Checker checker = doCheck(request);
        if (!checker.isValid()) {
            return checker.getResult();
        }
        try {
            // 配置广告显示规则
            String table = TableManager.getVersionTable(checker.getAppcode());
            //String locale = checker.getLocale();
            String locale = "zh-CN";
            List<Map<String, Object>> versionList = commonService.list(CommonSqlManager.queryVersion(table, locale));

            if (versionList.size() <= 0) {
                return JsonCryptUtil.makeSuccess();
            }
            Map<String, Object> mapData = versionList.get(0);
            return JsonCryptUtil.makeSuccess(mapData);
        } catch (Exception e){
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }
    }

    /**
     * 第三方云服务数据
     */
    private static Map<String, CloudData> mCloudData = new HashMap<>();

    @RequestMapping("/cloud/select")
    public String cloudSelect(HttpServletRequest request) {

        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        String cate = jsonObject.get(Column.CATE).getAsString();
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
        obj.add(Column.DATA, new Gson().toJsonTree(list));
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
        int uid = jsonObject.get(Column.UID).getAsInt();
        String cate = jsonObject.get(Column.CATE).getAsString();
        String text = jsonObject.get(Column.TEXT).getAsString();
        String model = jsonObject.get(Column.MODEL).getAsString();
        String ipaddr = jsonObject.get(Column.IPADDR).getAsString();
        CloudData bean = new CloudData(uid, cate, text, model, ipaddr);
        mCloudData.put(cate, bean);

        return JsonCryptUtil.makeSuccess();
    }

    /**
     * 用户反馈
     * @param request
     * @return
     */
    @RequestMapping("/feedback/add")
    public String feedbackAdd(HttpServletRequest request) {
        Checker pair = doCheck(request);
        if (!pair.isValid()) {
            return pair.getResult();
        }
        JsonObject jsonObject = pair.getDataObject();
        try {
            int uid = jsonObject.get(Column.UID).getAsInt();
            String appcode = jsonObject.get(Column.APPCODE).getAsString();
            String title = jsonObject.get(Column.TITLE).getAsString();
            String content = jsonObject.get(Column.CONTENT).getAsString();
            String contact = jsonObject.get(Column.CONTACT).getAsString();
            String brand = jsonObject.get(Column.BRAND).getAsString();
            String model = jsonObject.get(Column.MODEL).getAsString();
            String ipaddr = IPUtil.getIpAddress(request);
            String table = TableManager.getFeedbackTable(appcode);

            long curTime = getCurrentTimeMs();
            boolean flag = commonService.add(CommonSqlManager.addFeedback(table, uid, title, content, contact, brand, model, ipaddr, curTime, curTime));
            if (flag) {
                return JsonCryptUtil.makeSuccess();
            }
        } catch (Exception e){
            e.printStackTrace();
            return JsonCryptUtil.makeFail(e.getMessage());
        }

        return JsonCryptUtil.makeFail("unknown");
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

    /**
     * 服务器时间戳（s）
     * @return
     */
    @RequestMapping("/test")
    public String test() {
        return "hello!!";
    }

}
