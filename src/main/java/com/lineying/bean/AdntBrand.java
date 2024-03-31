package com.lineying.bean;

/**
 * 禁用广告品牌
 */
public class AdntBrand {

    private String appcode;
    private String platform;
    private String brand;
    private int status;

    public AdntBrand(String appcode, String platform, String brand, int status) {
        this.appcode = appcode;
        this.platform = platform;
        this.brand = brand;
        this.status = status;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AdntBrand{" +
                "appcode='" + appcode + '\'' +
                ", platform='" + platform + '\'' +
                ", brand='" + brand + '\'' +
                ", status=" + status +
                '}';
    }
}
