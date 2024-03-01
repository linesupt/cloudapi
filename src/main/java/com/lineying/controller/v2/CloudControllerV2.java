package com.lineying.controller.v2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lineying.bean.CloudData;
import com.lineying.controller.BaseController;
import com.lineying.controller.CheckPair;
import com.lineying.util.AESUtil;
import com.lineying.util.JsonCryptUtil;
import com.lineying.util.JsonUtil;
import com.lineying.util.SignUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lineying.common.SignResult.KEY_ERROR;
import static com.lineying.common.SignResult.SIGN_ERROR;

/**
 * 应用级接口
 */
@RestController
@RequestMapping("v2")
public class CloudControllerV2 extends BaseController {

    /**
     * 第三方云服务数据
     */
    private static Map<String, CloudData> mCloudData = new HashMap<>();

    @RequestMapping("/cloud/select")
    public String cloudSelect(HttpServletRequest request) {

        CheckPair pair = checkValid(request);
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

        CheckPair pair = checkValid(request);
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
