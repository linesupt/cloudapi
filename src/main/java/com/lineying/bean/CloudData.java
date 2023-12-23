package com.lineying.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 云数据
 */
public class CloudData {

    // 数据有效时间
    private static final long INTERVAL = 3600_000;

    // 用户ID
    private int uid;
    // 数据分类
    private String cate;
    // 接口内容
    private String text;
    // 上传设备型号
    private String model;
    // ip地址
    private String ipaddr;
    // 创建时间
    private long createTime;
    // 更新时间
    private long updateTime;

    public CloudData() {

    }

    public CloudData(int uid, String cate, String text, String model, String ipaddr) {
        this.uid = uid;
        this.cate = cate;
        this.text = text;
        this.model = model;
        this.ipaddr = ipaddr;
        this.createTime = System.currentTimeMillis();
        this.updateTime = createTime;
    }

    /**
     * 验证数据是否过期
     * @return
     */
    public boolean validate() {
        long duration = System.currentTimeMillis() - createTime;
        if (duration >= INTERVAL) {
            return false;
        }
        return true;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 转换为数据
     * @return
     */
    public Map<String, Object> toData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 0);
        map.put("uid", uid);
        map.put("cate", cate);
        map.put("text", text);
        map.put("model", model);
        map.put("ipaddr", ipaddr);
        map.put("create_time", createTime);
        map.put("update_time", updateTime);
        return map;
    }

}
