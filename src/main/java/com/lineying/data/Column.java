package com.lineying.data;

/**
 * 所有数据表数据列
 */
public @interface Column {


    // 查询所有字段
    String COLUMN_ALL = "*";
    String TABLE = "table";
    String WHERE = "where";
    String SET = "set";
    String SORT = "sort";
    String SORT_COLUMN = "sort_column";
    String COLUMN = "column";
    String VALUE = "value";
    String SQL = "sql";
    String TARGET = "target";
    String ORDER_INFO = "order_info";
    String TOTAL_FEE = "total_fee";
    String BODY = "body";
    String APP_ID = "app_id";

    // 倒序排序
    String SORT_DESC = "desc";
    // 顺序排序
    String SORT_ASC = "asc";

    // 数据表字段
    String ID = "id";
    String TOKEN = "token";
    String USERNAME = "username";
    String PASSWORD = "password";
    String PASSWORD_OLD = "password_old";
    String NICKNAME = "nickname";
    String MOBILE = "mobile";
    String EMAIL = "email";
    String SEX = "sex";
    String AVATAR = "avatar";
    String BRAND = "brand";
    String MODEL = "model";
    String VIP_FOREVER = "vip_forever";
    String EXPIRE_TIME = "expire_time";
    String APPLE_USER = "apple_user";
    String CREATE_TIME = "create_time";
    String UPDATE_TIME = "update_time";
    // 用户名最后修改时间
    String MODN_TIME = "modn_time";
    String UID = "uid";
    String SETTINGS = "settings";
    String DATA = "data";
    String SIGNATURE = "signature";
    String PAY_TYPE = "pay_type";
    String PLATFORM_TYPE = "platform_type";
    String TRADE_NO = "trade_no";
    String TRADE_STATUS = "trade_status";
    String OUT_TRADE_NO = "out_trade_no";
    String GOODS_CODE = "goods_code";
    String GOODS_ID = "goods_id";
    String CONTENT = "content";
    /**
     * 由bit位表示（00000011）, 1表示开启，0表示关闭，每一位代表一个平台
     * 低位第一位表示所有平台，1表示所有都开启，等于0时，继续判断平台标志位
     * 第2位表示Android，低位第3位代表iOS
     * 如果后续支持更多平台，依次开启后续位
     * 0表示所有都关闭，1：所有都开启、2：仅开启Android，4：仅开启iOS 6：开启Android和iOS
     */
    String STATUS = "status";
    String VERSION = "version";
    String APPCODE = "appcode";
    String CODE = "code";
    String NAME = "name";
    String DESCRIBE = "describe";
    String CURRENCY = "currency";
    String PRICE = "price";
    String PRICE_ORIGINAL = "price_original";
    String TYPE = "type"; // 产品类型 0内购 1订阅
    String PLATFORM = "platform"; // 系统平台
    String LOCALE = "locale"; // 地区
    String DURATION = "duration"; // 会员时长
    String AMOUNT = "amount"; // 兑换数量

    String CATE = "cate"; // 数据分类
    String TEXT = "text"; // 接口内容
    String IPADDR = "ipaddr"; // ip地址
    String TITLE = "title"; // 反馈标题
    String CONTACT = "contact"; // 联系方式
    String REPLY = "reply"; // 回复内容
    String IDENTITY_TOKEN = "identity_token"; // APPLE token
    String CLIENT_ID = "client_id"; // APPLE client_id
    String TIMESTAMP = "timestamp"; // 时间戳
    String REMAIN_INTERVAL = "remain_interval"; // 有效间隔

    // 支付宝支付
    String APPID = "appid";
    String PARTNER_ID = "partnerid";
    String PREPAY_ID = "prepayid";
    String NONCESTR = "noncestr";
    String PACKAGE = "package";
    String SIGN = "sign";

}
