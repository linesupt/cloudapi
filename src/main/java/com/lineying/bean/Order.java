package com.lineying.bean;

/**
 * 订单
 */
public class Order {

    public static final String TABLE = "order_gen";
    private int id;
    private int uid;
    private String appcode;
    private String goodsCode;
    private String outTradeNo;
    private String tradeNo;
    private String body;
    private String payType;
    private int status;
    private long createTime;
    private long updateTime;

    private String appid;
    private String totalFee;

    public Order() {

    }

    public Order(int uid, String appcode, String goodsCode, String outTradeNo,
                 String tradeNo, String body, String payType, int status, String appid, String totalFee, long createTime,
                 long updateTime) {
        this.uid = uid;
        this.appcode = appcode;
        this.goodsCode = goodsCode;
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
        this.body = body;
        this.payType = payType;
        this.status = status;
        this.appid = appid;
        this.totalFee = totalFee;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * 创建订单
     * @param uid
     * @param appcode
     * @param goodsCode
     * @param outTradeNo
     * @param body
     * @param payType
     * @return
     */
    public static Order makeOrder(int uid, String appcode, String goodsCode, String outTradeNo, String body, String payType, String appid, String totalFee) {
        return new Order(uid, appcode, goodsCode, outTradeNo, "", body, payType, 0, appid, totalFee, System.currentTimeMillis(), System.currentTimeMillis());
    }

    /**
     * 列
     * @return
     */
    public String getColumn() {
        String column = "`uid`,`appcode`,`goods_code`,`out_trade_no`,`trade_no`,`content`,`pay_type`,`status`,`create_time`,`update_time`";
        return column;
    }

    /**
     * 数据值
     * @return
     */
    public String getValue() {
        String value = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", uid + "", appcode,
                goodsCode, outTradeNo, tradeNo, body, payType, status + "", createTime + "", updateTime + "");
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
                ", goodsCode='" + goodsCode + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", body='" + body + '\'' +
                ", payType='" + payType + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", appid='" + appid + '\'' +
                ", totalFee='" + totalFee + '\'' +
                '}';
    }
}
