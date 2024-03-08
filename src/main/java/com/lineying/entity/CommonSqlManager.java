package com.lineying.entity;

import com.lineying.data.Column;

/**
 * sql查询管理
 */
public class CommonSqlManager {

    /**
     * 查询是否存在appleuser
     * @param table
     * @param appleUser
     * @return
     */
    public static CommonQueryEntity hasAppleUser(String table, String appleUser) {
        return hasFieldValue(table, Column.APPLE_USER, appleUser);
    }

    /**
     * 查询是否存在用户名
     * @param table
     * @param username
     * @return
     */
    public static CommonQueryEntity hasUsername(String table, String username) {
        return hasFieldValue(table, Column.USERNAME, username);
    }

    /**
     * 查询是否存在某个字段
     * @param table
     * @param field
     * @param value
     * @return
     */
    public static CommonQueryEntity hasFieldValue(String table, String field, String value) {
        String where = String.format("%s='%s'", field, value);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(field);
        entity.setWhere(where);
        entity.setSort("desc");
        entity.setSortColumn(field);
        return entity;
    }

}
