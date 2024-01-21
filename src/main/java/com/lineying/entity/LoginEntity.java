package com.lineying.entity;

/**
 * 登录
 */
public class LoginEntity {

    private String table;
    // 根据类型决定此字段代表什么
    private String username;
    private String password;
    private String value;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
