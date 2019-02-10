/*
Navicat MySQL Data Transfer

Source Server         : 本地mysql
Source Server Version : 50561
Source Host           : localhost:3306
Source Database       : marsday_jxc

Target Server Type    : MYSQL
Target Server Version : 50561
File Encoding         : 65001

Date: 2019-02-09 22:29:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jxc_next_customer
-- ----------------------------
DROP TABLE IF EXISTS `jxc_next_customer`;
CREATE TABLE `jxc_next_customer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jxc_next_customer
-- ----------------------------
INSERT INTO `jxc_next_customer` VALUES ('1', '陈先生', '1301234555', '上海', '黄浦', '0');
INSERT INTO `jxc_next_customer` VALUES ('2', '王小姐', '1254667886', '北京', '东城', '0');
INSERT INTO `jxc_next_customer` VALUES ('3', '张老板', '13145678901', '天津', '东丽', '0');
INSERT INTO `jxc_next_customer` VALUES ('4', '朱小姐', '13024688977', '天津', '河西', '0');
INSERT INTO `jxc_next_customer` VALUES ('5', '某某', '1345678901', '请选择省市', '请选择城市', '0');

-- ----------------------------
-- Table structure for jxc_next_daily_input
-- ----------------------------
DROP TABLE IF EXISTS `jxc_next_daily_input`;
CREATE TABLE `jxc_next_daily_input` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `pay_id` bigint(20) NOT NULL,
  `price` int(20) unsigned zerofill NOT NULL,
  `operationtime` date NOT NULL,
  `recordtime` date NOT NULL,
  `refer` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='日常收入';

-- ----------------------------
-- Records of jxc_next_daily_input
-- ----------------------------
INSERT INTO `jxc_next_daily_input` VALUES ('1', '坦直路拆迁', '3', '2', '00000000000000020000', '2019-02-09', '2019-02-09', '', '0');
INSERT INTO `jxc_next_daily_input` VALUES ('2', '坦仁路拆迁', '3', '0', '00000000000000040000', '2019-02-09', '2019-02-09', '', '0');

-- ----------------------------
-- Table structure for jxc_next_daily_output
-- ----------------------------
DROP TABLE IF EXISTS `jxc_next_daily_output`;
CREATE TABLE `jxc_next_daily_output` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `pay_id` bigint(20) NOT NULL,
  `price` int(20) unsigned zerofill NOT NULL,
  `operationtime` date NOT NULL,
  `recordtime` date NOT NULL,
  `refer` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='日常支出';

-- ----------------------------
-- Records of jxc_next_daily_output
-- ----------------------------
INSERT INTO `jxc_next_daily_output` VALUES ('1', '河道清洗', '1', '1', '00000000000000005000', '2019-02-09', '2019-02-09', '2/8已经施工结束', '0');
INSERT INTO `jxc_next_daily_output` VALUES ('2', '大门修理', '2', '0', '00000000000000002000', '2019-02-09', '2019-02-09', '', '0');
INSERT INTO `jxc_next_daily_output` VALUES ('3', '供氧铺设', '1', '4', '00000000000000006000', '2019-02-09', '2019-02-09', '', '0');
INSERT INTO `jxc_next_daily_output` VALUES ('4', '111', '1', '0', '00000000000000003000', '2019-02-09', '2019-02-09', '', '1');

-- ----------------------------
-- Table structure for jxc_next_pay
-- ----------------------------
DROP TABLE IF EXISTS `jxc_next_pay`;
CREATE TABLE `jxc_next_pay` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jxc_next_pay
-- ----------------------------
INSERT INTO `jxc_next_pay` VALUES ('1', '公共卡', '0');
INSERT INTO `jxc_next_pay` VALUES ('2', '交通卡', '0');
INSERT INTO `jxc_next_pay` VALUES ('3', '中国银行卡', '1');
INSERT INTO `jxc_next_pay` VALUES ('4', '马总卡', '0');

-- ----------------------------
-- Table structure for jxc_next_sales_input
-- ----------------------------
DROP TABLE IF EXISTS `jxc_next_sales_input`;
CREATE TABLE `jxc_next_sales_input` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `target_id` bigint(20) NOT NULL,
  `pay_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `price` int(20) unsigned zerofill NOT NULL,
  `volume` int(20) NOT NULL,
  `unit` varchar(255) NOT NULL,
  `grade` varchar(255) NOT NULL,
  `operationtime` date NOT NULL,
  `recordtime` date NOT NULL,
  `refer` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='销售收入';

-- ----------------------------
-- Records of jxc_next_sales_input
-- ----------------------------
INSERT INTO `jxc_next_sales_input` VALUES ('1', '1', '1', '1', '00000000000000003000', '10', '只', '1级', '2019-02-09', '2019-02-09', '', '0');
INSERT INTO `jxc_next_sales_input` VALUES ('2', '1', '4', '2', '00000000000000004000', '15', '只', '2级', '2019-02-09', '2019-02-09', '', '0');
INSERT INTO `jxc_next_sales_input` VALUES ('3', '2', '1', '3', '00000000000000000500', '2', '条', '一级', '2019-02-09', '2019-02-09', '', '0');

-- ----------------------------
-- Table structure for jxc_next_target
-- ----------------------------
DROP TABLE IF EXISTS `jxc_next_target`;
CREATE TABLE `jxc_next_target` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` tinyint(1) DEFAULT '0',
  `units` varchar(255) DEFAULT NULL,
  `grades` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jxc_next_target
-- ----------------------------
INSERT INTO `jxc_next_target` VALUES ('1', '蟹', '5', '只-盒-箱', '1级-2级-3级', '0');
INSERT INTO `jxc_next_target` VALUES ('2', '鱼', '5', '条-箱', '一级', '0');
INSERT INTO `jxc_next_target` VALUES ('3', '拆迁费', '2', '', '', '0');

-- ----------------------------
-- Table structure for jxc_next_user
-- ----------------------------
DROP TABLE IF EXISTS `jxc_next_user`;
CREATE TABLE `jxc_next_user` (
  `name_en` varchar(255) NOT NULL,
  `name_ch` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `type` tinyint(1) unsigned zerofill NOT NULL,
  `last_login` varchar(255) DEFAULT '',
  `del_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`name_en`,`name_ch`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of jxc_next_user
-- ----------------------------
INSERT INTO `jxc_next_user` VALUES ('xiaoli', '小李', '123456', '0', '', '0');
INSERT INTO `jxc_next_user` VALUES ('xiaoma', '小马', '123456', '1', '', '0');
