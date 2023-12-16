package com.lineying.common;

import java.util.Objects;

/**
 * 平台信息
 */
public enum Platform {
    ANDROID(0, "android"),
    iOS(1, "ios");

    int id;
    String code;
    Platform(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 通过标志符获取平台信息
     * @param platform
     * @return
     */
    public static Platform get(String platform) {
        if (Objects.isNull(platform)) {
            return ANDROID;
        }
        switch (platform) {
            case "android":
                return ANDROID;
            case "ios":
                return iOS;
        }
        return ANDROID;
    }

}
