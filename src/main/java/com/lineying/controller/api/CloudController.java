package com.lineying.controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.bean.CloudData;
import com.lineying.common.AppCodeManager;
import com.lineying.controller.BaseController;
import com.lineying.entity.CommonAddEntity;
import com.lineying.service.ICommonService;
import com.lineying.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.lineying.common.SignResult.KEY_ERROR;
import static com.lineying.common.SignResult.SIGN_ERROR;

/**
 * 应用级接口
 */
@RestController
@RequestMapping("api")
public class CloudController extends BaseController {

    // Apple授权登录
    private final static String authAppleUrl = "https://appleid.apple.com/auth/keys";
    private final static String authAppleIss = "https://appleid.apple.com";

    @Resource
    ICommonService commonService;

    /**
     * 第三方云服务数据
     */
    private static Map<String, CloudData> mCloudData = new HashMap<>();

    @RequestMapping("/cloud/select")
    public String cloudSelect(HttpServletRequest request) {

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
            return JsonUtil.makeFailTime();
        }
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
     * 用户反馈
     * @param request
     * @return
     */
    @RequestMapping("/feedback/add")
    public String feedbackAdd(HttpServletRequest request) {

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

        try {
            int uid = jsonObject.get("uid").getAsInt();
            String appcode = jsonObject.get("appcode").getAsString();
            String title = jsonObject.get("title").getAsString();
            String content = jsonObject.get("content").getAsString();
            String contact = jsonObject.get("contact").getAsString();
            String brand = jsonObject.get("brand").getAsString();
            String model = jsonObject.get("model").getAsString();
            String ipaddr = IPUtil.getIpAddress(request);
            String tableName = AppCodeManager.getFeedbackTable(appcode);

            long curTime = getCurrentTimeMs();
            String column = "`uid`,`title`,`content`,`contact`,`brand`,`model`,`ipaddr`,`create_time`,`update_time`";
            String value = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s'", uid, title, content, contact, brand, model, ipaddr, curTime, curTime);
            CommonAddEntity entityAdd = new CommonAddEntity();
            entityAdd.setTable(tableName);
            entityAdd.setColumn(column);
            entityAdd.setValue(value);
            boolean flag = commonService.add(entityAdd);
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
