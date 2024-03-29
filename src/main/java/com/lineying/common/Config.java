package com.lineying.common;

import com.lineying.bean.MediaPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 缓存配置
 */
public class Config {

    // 默认播放媒体
    public static String defMedia = "";
    // 媒体播放计划
    public static List<Map<String, Object>> mediaPlanList = new ArrayList<>();
    // 广告禁用的品牌设备
    public static List<String> adntBrandList = new ArrayList<>();

}
