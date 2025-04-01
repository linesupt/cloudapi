package com.lineying.entity;

/**
 * 接口日志
 */
public class ApiLog {

    // 应用
    String appcode;
    // 用户ID
    int uid;
    // 接口路径
    String uri;
    // 接口名
    String name;
    // body类型
    String contentType;
    // 请求头
    String header;
    // 请求参数
    String body;
    // 响应内容
    String data;
    // 设备型号
    String model;
    // IP地址
    String ipaddr;
    // 创建时间
    long createTime;
    // 更新时间
    long updateTime;

    /**
     * 列
     * @return
     */
    public String getColumn() {
        String column = String.format("`appcode`,`uid`,`uri`,`name`,`content_type`,`header`,`body`,`data`,`model`,`ipaddr`,`create_time`,`update_time`");
        return column;
    }

    /**
     * 数据值
     * @return
     */
    public String getValue() {
        String value = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'",
                appcode, uid + "", uri, name, contentType, header, body, data, model, ipaddr, createTime + "", updateTime + "");
        return value;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    @Override
    public String toString() {
        return "ApiLog{" +
                "appcode='" + appcode + '\'' +
                "uid='" + uid + '\'' +
                "uri='" + uri + '\'' +
                ", name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                ", header='" + header + '\'' +
                ", body='" + body + '\'' +
                ", data='" + data + '\'' +
                ", model='" + model + '\'' +
                ", ipaddr='" + ipaddr + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
