package com.lineying.entity;

import com.lineying.bean.Order;
import com.lineying.common.LoginType;
import com.lineying.common.TableManager;
import com.lineying.data.Column;
import com.lineying.util.JsonCryptUtil;

import java.awt.*;

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
     * 用户登录
     * @param table
     * @param username
     * @param password
     * @return
     */
    public static LoginEntity login(String table, String username, String password) {
        LoginEntity entity = new LoginEntity();
        entity.setTable(table);
        entity.setUsername(username);
        entity.setPassword(password);
        return entity;
    }

    /**
     * 用户登录
     * @param table
     * @param username
     * @param password
     * @return
     */
    public static LoginEntity loginForAppleUser(String table, String appleUser) {
        LoginEntity entity = new LoginEntity();
        entity.setTable(table);
        entity.setUsername(appleUser);
        return entity;
    }

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
     * 查询媒体播放计划
     * @param table
     * @param locale
     * @return
     */
    public static CommonQueryEntity queryMediaPlan() {
        String where = String.format("%s='%s'", Column.CYCLE_TYPE, "1");
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(TableManager.getMediaPlanTable());
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 查询媒体播放计划
     * @param table
     * @param locale
     * @return
     */
    public static CommonQueryEntity queryAdBrand() {
        String where = String.format("%s='%s'", Column.STATUS, "0");
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(TableManager.getAdBrandTable());
        entity.setColumn(Column.BRAND);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 查询默认播放媒体
     * @param table
     * @param locale
     * @return
     */
    public static CommonQueryEntity queryDefaultMedia() {
        String where = String.format("%s='%s' and %s='%s'", Column.CYCLE_TYPE, "" + 0, Column.START_TIME, "" + 0);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(TableManager.getMediaPlanTable());
        entity.setColumn(Column.TYPE);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 查询默认播放媒体
     * @param table
     * @param locale
     * @return
     */
    public static CommonQueryEntity queryVersion(String table, String locale) {
        String where = String.format("%s='%s' and %s=(select MAX(`%s`) from %s)", Column.LOCALE, locale, Column.CODE, Column.CODE, table);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 查询广告配置列表
     * @param table
     * @param locale
     * @return
     */
    public static CommonQueryEntity queryAdList(String appcode, String platform, String pkgname) {
        String where = String.format("%s='%s' and %s='%s' and %s='%s'", Column.APPCODE, appcode,
                Column.PLATFORM, platform, Column.PKGNAME, pkgname);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(TableManager.getAdTable());
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 查询商品列表
     * @param table
     * @param locale
     * @return
     */
    public static CommonQueryEntity queryOrder(String outTradeNo) {
        return queryAttrAll(Order.TABLE, Column.OUT_TRADE_NO, outTradeNo, Column.SORT_ASC, Column.ID);
    }

    /**
     * 查询商品列表
     * @param table
     * @param locale
     * @return
     */
    public static CommonQueryEntity queryGoodsList(String table, String locale) {
        return queryAttrAll(table, Column.LOCALE, locale, Column.SORT_ASC, Column.ID);
    }

    /**
     * 查询单条商品
     * @param table
     * @param code
     * @return
     */
    public static CommonQueryEntity queryGoods(String table, String code) {
        return queryAttrAll(table, Column.CODE, code, Column.SORT_ASC, Column.ID);
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
     * 查询列数据
     * @param table
     * @param column
     * @param value
     * @return
     */
    public static CommonQueryEntity queryAttrAll(String table, String column, String value, String sort, String sortColumn) {
        String where = String.format("%s='%s'", column, value);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
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
     * 查询列属性
     * @param table
     * @param uid
     * @param column
     * @param value
     * @return
     */
    public static CommonQueryEntity queryAttr(String table, String column, String value) {
        return queryFieldValue(table, column, value);
    }

    /**
     * 查询用户名
     * @param table
     * @param username
     * @return
     */
    public static CommonQueryEntity queryUsername(String table, String username) {
        return queryFieldValue(table, Column.USERNAME, username);
    }

    /**
     * 查询邮箱
     * @param table
     * @param email
     * @return
     */
    public static CommonQueryEntity queryEmail(String table, String email) {
        return queryFieldValue(table, Column.EMAIL, email);
    }

    /**
     * 查询手机号
     * @param table
     * @param mobile
     * @return
     */
    public static CommonQueryEntity queryMobile(String table, String mobile) {
        return queryFieldValue(table, Column.MOBILE, mobile);
    }

    /**
     * 查询Appleuser
     * @param table
     * @param appleUser
     * @return
     */
    public static CommonQueryEntity queryAppleUser(String table, String appleUser) {
        return queryFieldValue(table, Column.APPLE_USER, appleUser);
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
     * 通过ID查询密码
     * @param table
     * @param password
     * @param username
     * @return
     */
    public static CommonQueryEntity queryPasswordForUsername(String table, String password, String username) {
        return queryUserForField(table, password, Column.USERNAME, username);
    }

    /**
     * 查询用户
     * @param table
     * @param username
     * @param password
     * @return
     */
    public static CommonQueryEntity queryUser(String table, String username, String password) {
        String where = String.format("%s='%s' and %s='%s'", Column.USERNAME, username, Column.PASSWORD, password);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        entity.setWhere(where);
        return entity;
    }

    public static CommonQueryEntity queryUserForAppleUser(String table, String username, String password) {
        String where = String.format("%s='%s' and %s='%s'", Column.USERNAME, username, Column.APPLE_USER, password);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 通过邮箱查询用户
     * @param table
     * @param email
     * @param password
     * @return
     */
    public static CommonQueryEntity queryUserForEmail(String table, String email, String password) {
        String where = String.format("%s='%s' and %s='%s'", Column.EMAIL, email, Column.PASSWORD, password);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 通过手机号查询用户
     * @param table
     * @param mobile
     * @param password
     * @return
     */
    public static CommonQueryEntity queryUserForMobile(String table, String mobile, String password) {
        String where = String.format("%s='%s' and %s='%s'", Column.MOBILE, mobile, Column.PASSWORD, password);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
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
        String where = String.format("%s='%s'", Column.ID, uid + "");
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
        entity.setColumn(Column.COLUMN_ALL);
        entity.setSort(Column.SORT_DESC);
        entity.setSortColumn(Column.ID);
        entity.setWhere(where);
        return entity;
    }

    /**
     * 添加用户
     * @param table
     * @param username
     * @param password
     * @param appleUser
     * @param brand
     * @param model
     * @param ipaddr
     * @param createTime
     * @param updateTime
     * @return
     */
    public static CommonAddEntity addUser(String table, String username, String password,
                                          String appleUser, String brand, String model, String ipaddr,
                                          long createTime, long updateTime) {
        if ("".equals(appleUser)) { // 空的时候设置为NULL
            appleUser = "NULL";
        }
        String dataFormatColumn = "`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`";
        String dataFormatValue = "'%s','%s','%s','%s','%s','%s','%s','%s','%s'";
        if ("".equals(appleUser) || "NULL".equals(appleUser)) {
            dataFormatValue = "'%s','%s','%s',%s,'%s','%s','%s','%s','%s'";
        }
        String column = String.format(dataFormatColumn, Column.USERNAME, Column.NICKNAME, Column.PASSWORD, Column.APPLE_USER,
                Column.BRAND, Column.MODEL, Column.IPADDR, Column.CREATE_TIME, Column.UPDATE_TIME);
        String value = String.format(dataFormatValue, username, username, password, appleUser, brand, model, ipaddr, createTime + "", updateTime + "");
        CommonAddEntity entity = new CommonAddEntity();
        entity.setTable(table);
        entity.setColumn(column);
        entity.setValue(value);
        return entity;
    }

    /**
     * 删除用户
     * @param table
     * @param username
     * @return
     */
    public static CommonQueryEntity deleteUser(String table, String username) {
        String where = String.format("%s='%s'", Column.USERNAME, username);
        CommonQueryEntity entity = new CommonQueryEntity();
        entity.setTable(table);
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
        CommonUpdateEntity entity = new CommonUpdateEntity();
        entity.setTable(table);
        entity.setSet(set);
        entity.setWhere(where);
        return entity;
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
     * @param email
     * @return
     */
    public static CommonUpdateEntity updatePasswordForEmail(String table, String password, String email) {
        return updatePasswordForField(table, password, Column.EMAIL, email);
    }

    /**
     * 通过手机号更新密码
     * @param table
     * @param password
     * @param moile
     * @return
     */
    public static CommonUpdateEntity updatePasswordForMobile(String table, String password, String moile) {
        return updatePasswordForField(table, password, Column.MOBILE, moile);
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
        if ("".equals(value) || "NULL".equals(value)) {
            set = String.format("%s=NULL", column);
        }
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
     * 添加反馈建议
     * @param table
     * @param uid
     * @param title
     * @param content
     * @param contact
     * @param brand
     * @param model
     * @param ipaddr
     * @param createTime
     * @param updateTime
     * @return
     */
    public static CommonAddEntity addFeedback(String table, int uid, String title, String content, String contact, String brand, String model, String ipaddr, long createTime, long updateTime) {
        String column = String.format("`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`",
                Column.UID, Column.TITLE, Column.CONTENT, Column.CONTACT, Column.BRAND, Column.MODEL, Column.IPADDR,
                Column.CREATE_TIME, Column.UPDATE_TIME);
        String value = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s'", uid, title, content, contact, brand, model, ipaddr, createTime, updateTime);
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

    /**
     * 更新订单状态
     * @param tradeNo
     * @param outTradeNo
     * @param status
     * @param updateTime
     * @return
     */
    public static CommonUpdateEntity updateOrder(String tradeNo, String outTradeNo, int status, long updateTime) {
        CommonUpdateEntity entity = new CommonUpdateEntity();
        String set = String.format("%s='%s', %s='%s', %s='%s'", Column.TRADE_NO, tradeNo, Column.STATUS,
                status + "", Column.UPDATE_TIME, updateTime + "");
        String where = String.format("%s='%s'", Column.OUT_TRADE_NO, outTradeNo);
        entity.setSet(set);
        entity.setWhere(where);
        entity.setTable(Order.TABLE);
        return entity;
    }

}
