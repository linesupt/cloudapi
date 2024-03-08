package com.lineying.entity;

import com.lineying.data.Column;

/**
 * sql查询管理
 */
public class CommonSqlManager {

    /////////////////// 通用sql查询 start //////////////////////

    /**
     * 通用sql查询
     * @param table
     * @param column
     * @param where
     * @param sort
     * @param sortColumn
     * @return
     */
    public static CommonQueryEntity select(String table, String column, String where, String sort, String sortColumn) {
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setColumn(column);
        entity.setWhere(where);
        entity.setTable(table);
        entity.setSort(sort);
        entity.setSortColumn(sortColumn);
        return entity;
    }

    /**
     * 添加数据
     * @param table
     * @param column
     * @param value
     * @return
     */
    public static CommonAddEntity insert(String table, String column, String value) {
        CommonAddEntity entity = new CommonAddEntity();
        entity.setTable(table);
        entity.setColumn(column);
        entity.setValue(value);
        return entity;
    }

    /**
     * 处理删除查询
     * @param table
     * @param where
     * @return
     */
    public static CommonQueryEntity delete(String table, String where) {
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 更新数据查询
     * @param table
     * @param set
     * @param where
     * @return
     */
    public static CommonUpdateEntity update(String table, String set, String where) {
        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setTable(table);
        entity.setSet(set);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 执行任意命令
     * @param sql
     * @return
     */
    public static CommonCommandEntity command(String sql) {
        CommonCommandEntity entity = new CommonCommandEntity();
        entity.setRawSql(sql);
        return entity;
    }

    /////////////////// 通用sql查询 end //////////////////////

    /**
     * 查询是否存在appleuser
     * @param table
     * @param appleUser
     * @return
     */
    public static CommonQueryEntity hasAppleUser(String table, String appleUser) {
        return queryFieldValue(table, Column.APPLE_USER, appleUser);
    }

    /**
     * 查询是否存在用户名
     * @param table
     * @param username
     * @return
     */
    public static CommonQueryEntity hasUsername(String table, String username) {
        return queryFieldValue(table, Column.USERNAME, username);
    }

    /**
     * 查询商品列表
     * @param table
     * @param locale
     * @return
     */
    public static CommonQueryEntity queryGoodsList(String table, String locale) {
        return queryAttr(table, Column.LOCALE, locale, Column.SORT_ASC, Column.ID);
    }

    /**
     * 查询列属性
     * @param table
     * @param column
     * @param value
     * @return
     */
    public static CommonQueryEntity queryAttr(String table, String column, String value, String sort, String sortColumn) {
        String where = String.format("%s='%s'", column, value);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(column);
        entity.setWhere(where);
        entity.setSort(sort);
        entity.setSortColumn(sortColumn);
        return entity;
    }

    /**
     * 查询列属性
     * @param table
     * @param uid
     * @param column
     * @param value
     * @return
     */
    public static CommonQueryEntity queryAttr(String table, int uid, String column, String value) {
        return queryAttrForUid(table, uid, column, value, Column.SORT_DESC, column);
    }


    /**
     * 查询列属性
     * @param table
     * @param uid
     * @param column
     * @param value
     * @return
     */
    public static CommonQueryEntity queryAttrForUid(String table, int uid, String column, String value, String sort, String sortColumn) {
        String where = String.format("%s='%s' and %s='%s'", Column.ID, uid + "", column, value);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(column);
        entity.setWhere(where);
        entity.setSort(sort);
        entity.setSortColumn(sortColumn);
        return entity;
    }

    /**
     * 查询是否存在某个字段
     * @param table
     * @param field
     * @param value
     * @return
     */
    public static CommonQueryEntity queryFieldValue(String table, String field, String value) {
        String where = String.format("%s='%s'", field, value);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(field);
        entity.setWhere(where);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(field);
        return entity;
    }

    /**
     * 通过ID查询密码
     * @param table
     * @param password
     * @param id
     * @return
     */
    public static CommonQueryEntity queryPasswordForId(String table, String password, int id) {
        return queryUserForField(table, password, Column.ID, id + "");
    }

    /**
     * 通过email查询密码
     * @param table
     * @param password
     * @param email
     * @return
     */
    public static CommonQueryEntity queryPasswordForEmail(String table, String password, String email) {
        return queryUserForField(table, password, Column.EMAIL, email);
    }

    /**
     * 通过手机号查询密码
     * @param table
     * @param password
     * @param mobile
     * @return
     */
    public static CommonQueryEntity queryPasswordForMobile(String table, String password, String mobile) {
        return queryUserForField(table, password, Column.MOBILE, mobile);
    }

    /**
     * 配合字段查询了密码
     * @param table
     * @param password
     * @param field
     * @param value
     * @return
     */
    public static CommonQueryEntity queryUserForField(String table, String password, String field, String value) {
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(field);
        String where = String.format("%s='%s' and %s='%s'", field, value, Column.PASSWORD, password);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 查询用户
     * @param table
     * @param uid
     * @return
     */
    public static CommonQueryEntity queryUser(String table, int uid) {
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        String where = String.format("%s='%s'", Column.ID, uid + "");
        entity.setWhere(where);
        return entity;
    }

    /**
     * 查询兑换码
     * @param table
     * @param appcode
     * @param code
     * @return
     */
    public static CommonQueryEntity queryRedeem(String table, String appcode, String code) {
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);

        // 查询是否存在未使用的兑换码
        String where = String.format("%s='%s' and %s='%s' and %s='%s'", Column.CODE, code, Column.APPCODE, appcode, Column.STATUS, 0 + "");
        entity.setWhere(where);
        return entity;
    }

    /**
     * 消耗验证码
     * @param table
     * @param appcode
     * @param code
     * @return
     */
    public static CommonUpdateEntity consumeRedeem(String table, String appcode, String code) {
        String where = String.format("%s='%s' and %s='%s' and %s='%s'", Column.CODE, code, Column.APPCODE, appcode, Column.STATUS, 0 + "");
        String set = String.format("%s='%s'", Column.STATUS, 1 + "");
        CommonUpdateEntity updateEntity = new CommonUpdateEntity();
        updateEntity.setTable(table);
        updateEntity.setSet(set);
        updateEntity.setWhere(where);
    }

    /**
     * 通过uid更新密码
     * @param table
     * @param password
     * @param uid
     * @return
     */
    public static CommonUpdateEntity updatePasswordForUid(String table, String password, int uid) {
        return updatePasswordForField(table, password, Column.ID, uid + "");
    }

    /**
     * 通过邮箱更新密码
     * @param table
     * @param password
     * @param id
     * @return
     */
    public static CommonUpdateEntity updatePasswordForEmail(String table, String password, String id) {
        return updatePasswordForField(table, password, Column.EMAIL, id);
    }

    /**
     * 通过手机号更新密码
     * @param table
     * @param password
     * @param id
     * @return
     */
    public static CommonUpdateEntity updatePasswordForMobile(String table, String password, String id) {
        return updatePasswordForField(table, password, Column.MOBILE, id);
    }

    /**
     * 通过字段更新密码
     * @param table
     * @param password
     * @param field
     * @param value
     * @return
     */
    public static CommonUpdateEntity updatePasswordForField(String table, String password, String field, String value) {
        String where = String.format("%s='%s'", field, value);
        String set = String.format("%s='%s'", Column.PASSWORD, password);
        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setSet(set);
        entity.setWhere(where);
        entity.setTable(table);
        return entity;
    }

    /**
     * 更新会员有效时间
     * @param table
     * @param uid
     * @param expireTime
     * @return
     */
    public static CommonUpdateEntity updateExpireTime(String table, int uid, long expireTime) {
        String where = String.format("%s='%s'", Column.ID, uid + "");
        String set = String.format("%s='%s'", Column.EXPIRE_TIME, expireTime + "");
        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setTable(table);
        entity.setSet(set);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 通过uid更新字段
     * @param table
     * @param uid
     * @param column
     * @param value
     * @return
     */
    public static CommonUpdateEntity updateAttr(String table, int uid, String column, String value) {
        String where = String.format("%s='%s'", Column.ID, uid + "");
        String set = String.format("%s='%s'", column, value);
        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setSet(set);
        entity.setWhere(where);
        entity.setTable(table);
        return entity;
    }

    /**
     * 添加列数据到表中
     * @param table
     * @param column
     * @param value
     * @return
     */
    public static CommonAddEntity addColumnData(String table, String column, String value) {
        CommonAddEntity entity = new CommonAddEntity();
        entity.setTable(table);
        entity.setColumn(column);
        entity.setValue(value);
        return entity;
    }

    /////////////////设置用户表/////////////////

    /**
     * 查询用户设置
     * @param table
     * @param uid
     * @return
     */
    public static CommonQueryEntity queryUserSetting(String table, int uid) {
        String where = String.format("%s='%s'", Column.UID, uid + "");
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setWhere(where);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        return entity;
    }

    /**
     * 添加用户设置
     * @param table
     * @param uid
     * @param settings
     * @param data
     * @return
     */
    public static CommonAddEntity addUserSetting(String table, int uid, String settings, String data) {
        String column = String.format("`%s`,`%s`,`%s`", Column.UID, Column.SETTINGS, Column.DATA);
        String value = String.format("'%s','%s','%s'", uid + "", settings, data);
        CommonAddEntity entity = new CommonAddEntity();
        entity.setTable(table);
        entity.setColumn(column);
        entity.setValue(value);
        return entity;
    }

    /**
     * 更新设置表
     * @param table
     * @param uid
     * @param settings
     * @param data
     * @return
     */
    public static CommonUpdateEntity updateUserSetting(String table, int uid, String settings, String data) {
        String set = String.format("%s='%s', %s='%s'", Column.SETTINGS, settings, Column.DATA, data);
        String where = String.format("%s='%s'", Column.UID, uid + "");
        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setTable(table);
        entity.setSet(set);
        entity.setWhere(where);
        return entity;
    }


}
