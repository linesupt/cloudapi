package com.lineying.bean;

import com.lineying.data.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 媒体播放计划
 */
public class MediaPlan {

    private int id;
    private String type;
    private int cycleType;
    private long startTime;
    private long endTime;
    private int duration;
    private long createTime;
    private long updateTime;

    public MediaPlan() {

    }

    public MediaPlan(int id, String type, int cycleType, long startTime, long endTime,
                     int duration, long createTime, long updateTime) {
        this.id = id;
        this.type = type;
        this.cycleType = cycleType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * 解析计划列表
     * @param list
     * @return
     */
    public static List<MediaPlan> parse(List<Map<String, Object>> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<MediaPlan> results = new ArrayList<>();
        try {
            for (Map<String, Object> data : list) {
                int id = (Integer) data.get(Column.ID);
                String type = (String) data.get(Column.TYPE);
                int cycleType = (Integer) data.get(Column.CYCLE_TYPE);
                long startTime = (Long) data.get(Column.START_TIME);
                long endTime = (Long) data.get(Column.END_TIME);
                int duration = (Integer) data.get(Column.DURATION);
                long createTime = (Long) data.get(Column.CREATE_TIME);
                long updateTime = (Long) data.get(Column.UPDATE_TIME);
                MediaPlan plan = new MediaPlan(id, type, cycleType, startTime, endTime, duration, createTime, updateTime);
                results.add(plan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCycleType() {
        return cycleType;
    }

    public void setCycleType(int cycleType) {
        this.cycleType = cycleType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    @Override
    public String toString() {
        return "MediaPlan{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", cycleType=" + cycleType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
