/*
 Navicat Premium Data Transfer

 Source Server         : lineying.vip-root
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : lineying.vip
 Source Database       : recipe

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : utf-8

 Date: 05/10/2020 19:31:32 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `food_nutrition_info`
-- ----------------------------
DROP TABLE IF EXISTS `food_nutrition_info`;
CREATE TABLE `food_nutrition_info` (
  `id` int(11) NOT NULL,
  `code` varchar(512) DEFAULT NULL,
  `name` varchar(512) DEFAULT NULL,
  `calory` varchar(10) DEFAULT NULL,
  `weight` varchar(10) DEFAULT NULL,
  `health_light` varchar(10) DEFAULT NULL,
  `is_liquid` varchar(10) DEFAULT NULL,
  `thumb_image_url` varchar(255) DEFAULT NULL,
  `large_image_url` varchar(255) DEFAULT NULL,
  `uploader` varchar(10) DEFAULT NULL,
  `appraise` varchar(512) DEFAULT NULL,
  `protein` varchar(10) DEFAULT NULL,
  `fat` varchar(10) DEFAULT NULL,
  `fiber_dietary` varchar(10) DEFAULT NULL,
  `carbohydrate` varchar(10) DEFAULT NULL,
  `gi` varchar(10) DEFAULT NULL,
  `gl` varchar(10) DEFAULT NULL,
  `recipe` varchar(10) DEFAULT NULL,
  `recipe_type` varchar(10) DEFAULT NULL,
  `comments_ct` varchar(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `material_class`
-- ----------------------------
DROP TABLE IF EXISTS `material_class`;
CREATE TABLE `material_class` (
  `bid` int(11) NOT NULL AUTO_INCREMENT,
  `id` int(11) NOT NULL,
  `code` varchar(512) DEFAULT NULL,
  `name` varchar(512) DEFAULT NULL,
  `thumb_image_url` varchar(255) DEFAULT NULL,
  `is_liquid` varchar(10) DEFAULT NULL,
  `health_light` varchar(10) DEFAULT NULL COMMENT '健康指数',
  `weight` varchar(10) DEFAULT NULL COMMENT '分量',
  `calory` varchar(10) DEFAULT NULL COMMENT '卡路里',
  `fat` varchar(10) DEFAULT NULL COMMENT '脂肪',
  `protein` varchar(10) DEFAULT NULL COMMENT '蛋白质',
  `fiber_dietary` varchar(10) DEFAULT NULL COMMENT '膳食纤维',
  `carbohydrate` varchar(10) DEFAULT NULL COMMENT '碳水化合物',
  `carotene` varchar(10) DEFAULT '0.00' COMMENT '胡萝卜素',
  `vitamin_a` varchar(10) DEFAULT '0.00' COMMENT '维生素A',
  `vitamin_b1` varchar(10) DEFAULT NULL COMMENT '维生素B4',
  `vitamin_b2` varchar(10) DEFAULT NULL COMMENT '维生素B3',
  `thiamine` varchar(10) DEFAULT NULL,
  `lactoflavin` varchar(10) DEFAULT '0.00',
  `vitamin_c` varchar(10) DEFAULT '0.00' COMMENT '维生素C',
  `vitamin_e` varchar(10) DEFAULT NULL COMMENT '维生素E',
  `niacin` varchar(10) DEFAULT NULL COMMENT '烟酸',
  `natrium` varchar(10) DEFAULT NULL COMMENT '钠',
  `calcium` varchar(10) DEFAULT NULL COMMENT '钙',
  `iron` varchar(10) DEFAULT NULL COMMENT '铁',
  `kalium` varchar(10) DEFAULT NULL COMMENT '钾',
  `iodine` varchar(10) DEFAULT NULL COMMENT '碘',
  `zinc` varchar(10) DEFAULT NULL COMMENT '锌',
  `selenium` varchar(10) DEFAULT '0.00' COMMENT '硒',
  `magnesium` varchar(10) DEFAULT NULL COMMENT '镁',
  `copper` varchar(10) DEFAULT NULL COMMENT '铜',
  `manganese` varchar(10) DEFAULT NULL COMMENT '锰',
  `cholesterol` varchar(10) DEFAULT NULL COMMENT '胆固醇',
  `main_class_id` int(11) DEFAULT NULL COMMENT '主分类id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`bid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2302 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `material_main_class`
-- ----------------------------
DROP TABLE IF EXISTS `material_main_class`;
CREATE TABLE `material_main_class` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `menu_class`
-- ----------------------------
DROP TABLE IF EXISTS `menu_class`;
CREATE TABLE `menu_class` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `classid` varchar(30) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `parentid` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=626 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `menu_info`
-- ----------------------------
DROP TABLE IF EXISTS `menu_info`;
CREATE TABLE `menu_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `sid` varchar(30) DEFAULT NULL COMMENT '第三方id',
  `name` varchar(50) DEFAULT NULL COMMENT '菜名称',
  `peoplenum` varchar(50) DEFAULT NULL COMMENT '适合人数',
  `classid` varchar(30) DEFAULT NULL COMMENT '菜类型',
  `preparetime` varchar(50) DEFAULT NULL COMMENT '准备时间',
  `cookingtime` varchar(50) DEFAULT NULL COMMENT '煮所需要时间',
  `content` varchar(2048) DEFAULT NULL COMMENT '内容',
  `pic` varchar(255) DEFAULT NULL COMMENT '菜品封面图片',
  `tag` varchar(200) DEFAULT NULL COMMENT '标签',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `pic_isEmpty` int(1) DEFAULT NULL COMMENT '''图片是否存在(1存在，0不存在)'',',
  `is_recommend` int(1) DEFAULT NULL COMMENT '是否被推荐(1被推荐，0未推荐)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `sid` (`sid`) USING BTREE,
  KEY `info_info` (`classid`)
) ENGINE=InnoDB AUTO_INCREMENT=66468 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `menu_material`
-- ----------------------------
DROP TABLE IF EXISTS `menu_material`;
CREATE TABLE `menu_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `mid` int(20) DEFAULT NULL COMMENT '菜单信息表id',
  `mname` varchar(100) DEFAULT NULL COMMENT '调料名称',
  `type` int(3) DEFAULT NULL COMMENT '调料类型',
  `amount` varchar(100) DEFAULT NULL COMMENT '用量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `material_index` (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=614169 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `menu_process`
-- ----------------------------
DROP TABLE IF EXISTS `menu_process`;
CREATE TABLE `menu_process` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `mid` int(20) DEFAULT NULL COMMENT '菜单id',
  `pcontent` varchar(500) DEFAULT NULL COMMENT '流程',
  `pic` varchar(255) DEFAULT NULL COMMENT '图片',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `pic_isEmpty` int(1) DEFAULT '1' COMMENT '图片是否存在(1存在，0不存在)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `process_index` (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=642209 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `nutrition_compare`
-- ----------------------------
DROP TABLE IF EXISTS `nutrition_compare`;
CREATE TABLE `nutrition_compare` (
  `id` int(11) NOT NULL,
  `unit1` varchar(50) DEFAULT NULL,
  `target_name` varchar(50) DEFAULT NULL,
  `target_image_url` varchar(255) DEFAULT NULL,
  `amount0` varchar(10) DEFAULT NULL,
  `unit0` varchar(50) DEFAULT NULL,
  `amount1` varchar(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `nutrition_health_appraise`
-- ----------------------------
DROP TABLE IF EXISTS `nutrition_health_appraise`;
CREATE TABLE `nutrition_health_appraise` (
  `id` int(10) NOT NULL,
  `health_mode` varchar(10) DEFAULT NULL,
  `show1` varchar(10) DEFAULT NULL,
  `light` varchar(10) DEFAULT NULL,
  `appraise` varchar(512) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `nutrition_ingredient`
-- ----------------------------
DROP TABLE IF EXISTS `nutrition_ingredient`;
CREATE TABLE `nutrition_ingredient` (
  `id` int(10) DEFAULT NULL,
  `calory` varchar(10) DEFAULT NULL,
  `protein` varchar(10) DEFAULT NULL,
  `fat` varchar(10) DEFAULT NULL,
  `carbohydrate` varchar(10) DEFAULT NULL,
  `fiber_dietary` varchar(10) DEFAULT NULL,
  `vitamin_a` varchar(10) DEFAULT NULL,
  `vitamin_c` varchar(10) DEFAULT NULL,
  `vitamin_e` varchar(10) DEFAULT NULL,
  `carotene` varchar(10) DEFAULT NULL,
  `thiamine` varchar(10) DEFAULT NULL,
  `lactoflavin` varchar(10) DEFAULT NULL,
  `niacin` varchar(10) DEFAULT NULL,
  `cholesterol` varchar(10) DEFAULT NULL,
  `magnesium` varchar(10) DEFAULT NULL,
  `calcium` varchar(10) DEFAULT NULL,
  `iron` varchar(10) DEFAULT NULL,
  `zinc` varchar(10) DEFAULT NULL,
  `copper` varchar(10) DEFAULT NULL,
  `manganese` varchar(10) DEFAULT NULL,
  `kalium` varchar(10) DEFAULT NULL,
  `phosphor` varchar(10) DEFAULT NULL,
  `natrium` varchar(10) DEFAULT NULL,
  `selenium` varchar(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `nutrition_lights`
-- ----------------------------
DROP TABLE IF EXISTS `nutrition_lights`;
CREATE TABLE `nutrition_lights` (
  `id` int(10) NOT NULL,
  `calory` varchar(50) DEFAULT NULL,
  `protein` varchar(50) DEFAULT NULL,
  `carbohydrate` varchar(50) DEFAULT NULL,
  `flat` varchar(50) DEFAULT NULL,
  `fiber_dietary` varchar(50) DEFAULT NULL,
  `gi` varchar(50) DEFAULT NULL,
  `gl` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `nutrition_units`
-- ----------------------------
DROP TABLE IF EXISTS `nutrition_units`;
CREATE TABLE `nutrition_units` (
  `id` int(10) NOT NULL,
  `unit_id` varchar(10) DEFAULT NULL,
  `amount` varchar(10) DEFAULT NULL,
  `unit` varchar(20) DEFAULT NULL,
  `unit_name` varchar(20) DEFAULT NULL,
  `weight` varchar(10) DEFAULT NULL,
  `eat_weight` varchar(10) DEFAULT NULL,
  `calory` varchar(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT '' COMMENT '用户名',
  `mobile` varchar(15) DEFAULT '' COMMENT '手机号',
  `email` varchar(50) DEFAULT '' COMMENT '邮箱',
  `password` varchar(50) DEFAULT '' COMMENT '密码',
  `insert_uid` int(11) DEFAULT NULL COMMENT '添加该用户的用户id',
  `insert_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除（0：正常；1：已删）',
  `is_job` tinyint(1) DEFAULT '0' COMMENT '是否在职（0：正常；1，离职）',
  `mcode` varchar(10) DEFAULT '' COMMENT '短信验证码',
  `send_time` datetime DEFAULT NULL COMMENT '短信发送时间',
  `version` int(10) DEFAULT '0' COMMENT '更新版本',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `name` (`username`) USING BTREE,
  KEY `id` (`id`) USING BTREE,
  KEY `mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户表';

SET FOREIGN_KEY_CHECKS = 1;
