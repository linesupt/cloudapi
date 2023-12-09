package com.lineying.unitconverter.ui.account

/**
 * @author Tony
 * @datetime 2023/5/27 23:26
 * @version v1.0
 * @Description: 云数据库表管理
 */
annotation class Table {

    companion object {
        const val TableSuffix = "_cal"
    }

    // 用户表
    annotation class User {
        companion object {
            const val TABLE_NAME = "user$TableSuffix"
            const val ID = "id"
            const val USERNAME = "username"
            const val PASSWORD = "password"
            const val NICKNAME = "nickname"
            const val MOBILE = "mobile"
            const val EMAIL = "email"
            const val SEX = "sex"
            const val AVATAR = "avatar"
            const val BRAND = "brand"
            const val MODEL = "model"
            const val VIP_FOREVER = "vip_forever"
            const val EXPIRE_TIME = "expire_time"
            const val APPLE_USER = "apple_user"
            const val CREATE_TIME = "create_time"
            const val UPDATE_TIME = "update_time"
        }
    }

    // 用户设置表
    annotation class UserSetting {
        companion object {
            const val TABLE_NAME = "user_setting$TableSuffix"
            const val ID = "id"
            const val UID = "uid"
            const val SETTINGS = "settings"
            const val DATA = "data"
            const val CREATE_TIME = "create_time"
            const val UPDATE_TIME = "update_time"
        }
    }

    // 订单
    annotation class Order {
        companion object {
            const val TABLE_NAME = "order$TableSuffix"
            const val ID = "id"
            const val UID = "uid"
            const val PAY_TYPE = "pay_type"
            const val PLATFORM_TYPE = "platform_type"
            const val TRADE_NO = "trade_no"
            const val GOODS_ID = "goods_id"
            const val CONTENT = "content"
            const val STATUS = "status"
            const val BRAND = "brand"
            const val MODEL = "model"
            const val VERSION = "version"
            const val CREATE_TIME = "create_time"
            const val UPDATE_TIME = "update_time"
        }
    }

    // 商品
    annotation class Goods {
        companion object {
            const val TABLE_NAME = "goods$TableSuffix"
            const val ID = "id"
            const val CODE = "code"
            const val NAME = "name"
            const val DESCRIBE = "describe"
            const val CURRENCY = "currency"
            const val PRICE = "price"
            const val PRICE_ORIGINAL = "price_original"
            const val TYPE = "type" // 产品类型 0内购 1订阅
            const val LOCALE = "locale" // 地区
            const val DURATION = "duration" // 会员时长
            /**
             * 由bit位表示（00000011）, 1表示开启，0表示关闭，每一位代表一个平台
             * 低位第一位表示所有平台，1表示所有都开启，等于0时，继续判断平台标志位
             * 第2位表示Android，低位第3位代表iOS
             * 如果后续支持更多平台，依次开启后续位
             * 0表示所有都关闭，1：所有都开启、2：仅开启Android，4：仅开启iOS 6：开启Android和iOS
             */
            const val STATUS = "status"
            const val CREATE_TIME = "create_time"
            const val UPDATE_TIME = "update_time"
        }
    }

    // 兑换码
    annotation class RedeemCode {
        companion object {
            const val TABLE_NAME = "redeem_code$TableSuffix"
            const val ID = "id"
            const val CODE = "code" // 兑换码
            const val TYPE = "type" // 0: 时长 1: 数量
            const val AMOUNT = "amount" // 兑换数量
            const val STATUS = "status" // 0未使用，已使用
            const val UID = "uid" // 使用的用户
            const val DESCRIBE = "describe" // 描述
            const val CREATE_TIME = "create_time"
            const val UPDATE_TIME = "update_time"
        }
    }

    /// 云市场API接口数据表
    annotation class CloudData {
        companion object {
            const val TABLE_NAME = "cloud_data$TableSuffix"
            const val ID = "id"
            const val UID = "uid"
            const val CATE = "cate" // 数据分类
            const val TEXT = "text" // 接口内容
            const val MODEL = "model" // 上传设备型号
            const val IPADDR = "ipaddr" // ip地址
            const val CREATE_TIME = "create_time"
            const val UPDATE_TIME = "update_time"
        }
    }

    /// 用户反馈数据表
    annotation class Feedback {
        companion object {
            const val TABLE_NAME = "feecback$TableSuffix"
            const val ID = "id"
            const val UID = "uid"
            const val TITLE = "title" // 反馈标题
            const val CONTENT = "content" // 反馈内容
            const val CONTACT = "contact" // 联系方式
            const val REPLY = "reply" // 回复内容
            const val MODEL = "model" // 设备型号
            const val IPADDR = "ipaddr" // ip地址
            const val CREATE_TIME = "create_time"
            const val UPDATE_TIME = "update_time"
        }
    }

    // 验证码，找回密码
    annotation class VerifyCode {
        companion object {
            const val TABLE_NAME = "verify_code$TableSuffix"
            const val ID = "id"
            const val UID = "uid"
            const val CODE = "code" // 验证码
            const val TARGET = "target" // 目标：邮箱或手机
            const val TYPE = "type" // 1:邮箱验证 2:短信验证
            const val CREATE_TIME = "create_time"
            const val UPDATE_TIME = "update_time"
        }
    }

    // 日志
    annotation class Log {
        companion object {
            const val TABLE_NAME = "log$TableSuffix"
            const val ID = "id"
            const val TEXT = "text"
            const val CREATE_TIME = "create_time"
            const val UPDATE_TIME = "update_time"
        }
    }

    enum class CloudDataCate(val cate: String, val interval: Long) {
        EXCHANGE("exchange", 3600_000)
    }

}