package com.lineying.entity;

import lombok.Data;

@Data
public class CommonCommandEntity {

    // 原sql执行语句
    private String rawSql;

    public String getRawSql() {
        return rawSql;
    }

    public void setRawSql(String rawSql) {
        this.rawSql = rawSql;
    }

}
