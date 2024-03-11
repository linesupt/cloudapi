package com.lineying.controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lineying.bean.CloudData;
import com.lineying.common.AppCodeManager;
import com.lineying.controller.BaseController;
import com.lineying.controller.Checker;
import com.lineying.data.Column;
import com.lineying.data.Param;
import com.lineying.entity.CommonSqlManager;
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
            String table = AppCodeManager.getFeedbackTable(appcode);

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
