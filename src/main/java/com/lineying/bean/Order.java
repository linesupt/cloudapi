package com.lineying.bean;

import com.lineying.data.Column;

/**
 * 订单表，包含多个APP
 */
public class Order {

    public static final String TABLE = "order_gen";
    private int id;
    private int uid;
    private String appcode;
    private long goodsId;
    private String goodsCode;
    private String outTradeNo;
    private String tradeNo;
    private String originalTradeNo;
    private String body;
    private String payType;
    private int status;
    private long createTime;
    private long updateTime;

    // 第三方平台的应用ID
    private String appid;
    // 总金额
    private String totalFee;
    // 应用版本
    private String appVersion;

    public Order() {

    }

    public Order(int uid, String appcode, long goodsId, String goodsCode, String outTradeNo,
                 String tradeNo, String originalTradeNo, String body, String payType, int status,
                 String appid, String totalFee, String appVersion, long createTime,
                 long updateTime) {
        this.uid = uid;
        this.appcode = appcode;
        this.goodsId = goodsId;
        this.goodsCode = goodsCode;
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
        this.originalTradeNo = originalTradeNo;
        this.body = body;
        this.payType = payType;
        this.status = status;
        this.appid = appid;
        this.totalFee = totalFee;
        this.appVersion = appVersion;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * 创建订单
     * @param uid
     * @param appcode
     * @param goodsId
     * @param goodsCode
     * @param outTradeNo
     * @param body
     * @param payType
     * @return
     */
    public static Order makeOrder(int uid, String appcode, long goodsId, String goodsCode, String outTradeNo, String tradeNo, String originalTradeNo, String body, String payType, String appid, String totalFee, String appVersion) {
        long timestamp = System.currentTimeMillis();
        return new Order(uid, appcode, goodsId, goodsCode, outTradeNo, tradeNo, originalTradeNo, body, payType, 0, appid, totalFee, appVersion, timestamp, timestamp);
    }

    /**
     * 列
     * @return
     */
    public String getColumn() {
        String column = String.format("`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`",
                Column.UID, Column.APPCODE, Column.GOODS_ID, Column.GOODS_CODE, Column.OUT_TRADE_NO, Column.TRADE_NO,
                Column.ORIGINAL_TRADE_NO, Column.CONTENT, Column.PAY_TYPE, Column.STATUS, Column.APP_VERSION, Column.CREATE_TIME, Column.UPDATE_TIME);
        if ("".equals(outTradeNo)) {
            column = String.format("`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`",
                    Column.UID, Column.APPCODE, Column.GOODS_ID, Column.GOODS_CODE, Column.TRADE_NO,
                    Column.ORIGINAL_TRADE_NO, Column.CONTENT, Column.PAY_TYPE, Column.STATUS, Column.APP_VERSION, Column.CREATE_TIME, Column.UPDATE_TIME);
        }
        return column;
    }

    /**
     * 数据值
     * @return
     */
    public String getValue() {
        String value = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", uid + "", appcode, goodsId + "",
                goodsCode, outTradeNo, tradeNo, originalTradeNo, body, payType, status + "", appVersion, createTime + "", updateTime + "");
        if ("".equals(outTradeNo)) {
            value = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", uid + "", appcode, goodsId + "",
                    goodsCode, tradeNo, originalTradeNo, body, payType, status + "", appVersion, createTime + "", updateTime + "");
        }
        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
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
        return "Order{" +
                "id=" + id +
                ", uid=" + uid +
                ", appcode='" + appcode + '\'' +
                ", goodsId=" + goodsId +
                ", goodsCode='" + goodsCode + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", originalTradeNo='" + originalTradeNo + '\'' +
                ", body='" + body + '\'' +
                ", payType='" + payType + '\'' +
                ", status=" + status +
                ", appid='" + appid + '\'' +
                ", totalFee='" + totalFee + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
