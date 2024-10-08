package com.lineying.common;

/**
 * 收据验证结果码
 */
public @interface ReceiptCode {

    /**
     * 验证正常
     */
    int STATUS_OK = 0;
    /**
     * 对 App Store 的请求不是使用 HTTP POST 请求方法发出的
     */
    int STATUS_21000 = 21000;
    /**
     * App Store 不再发送此状态代码
     */
    int STATUS_21001 = 21001;
    /**
     * receipt-data属性中的数据格式错误或服务遇到临时问题。再试一次
     */
    int STATUS_21002 = 21002;
    /**
     * 收据无法验证
     */
    int STATUS_21003 = 21003;
    /**
     * 您提供的共享机密与您帐户中存档的共享机密不匹配
     */
    int STATUS_21004 = 21004;
    /**
     * 收据服务器暂时无法提供收据。再试一次
     */
    int STATUS_21005 = 21005;
    /**
     * 此收据有效，但订阅已过期。
     * 当此状态代码返回到您的服务器时，接收数据也会被解码并作为响应的一部分返回。
     * 仅针对自动续订订阅的iOS6样式交易收据返回
     */
    int STATUS_21006 = 21006;
    /**
     * 这个收据是来自测试环境，但是是送到生产环境去验证的
     */
    int STATUS_21007 = 21007;
    /**
     *  这个收据是来自生产环境，但是被送到了测试环境进行验证
     */
    int STATUS_21008 = 21008;
    /**
     * 内部数据访问错误。稍后再试
     */
    int STATUS_21009 = 21009;
    /**
     * 用户帐户无法找到或已被删除
     */
    int STATUS_21010 = 21010;

    /**
     * 状态代码21100-21199是各种内部数据访问错误。
     */
}
