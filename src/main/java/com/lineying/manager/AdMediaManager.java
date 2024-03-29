package com.lineying.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广告媒体管理
 */
public class AdMediaManager {

    /**
     * 创建空广告列表
     * @return
     */
    public static List<Map<String, Object>> makeEmptyAdList() {
        List<Map<String, Object>> adlist = new ArrayList<>();
        adlist.add(makeEmptyAd("gdt"));
        adlist.add(makeEmptyAd("gromore"));
        adlist.add(makeEmptyAd("admob"));
        return adlist;
    }

    /**
     * 创建空广告配置、用于屏蔽广告
     * @param adtype
     * @return
     */
    public static Map<String, Object> makeEmptyAd(String adtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("adtype", adtype);
        map.put("id", 0);
        map.put("appcode", "0");
        map.put("platform", "0");
        map.put("appid", "0");
        map.put("splash_id", "0");
        map.put("interstitial_id", "0");
        map.put("interstitial_pro_id", "0");
        map.put("banner_id", "0");
        map.put("reward_video_id", "0");
        map.put("native_id", "0");
        return map;
    }

}
