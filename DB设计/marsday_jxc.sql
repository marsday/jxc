/*
Navicat MySQL Data Transfer

Source Server         : 本地mysql
Source Server Version : 50561
Source Host           : localhost:3306
Source Database       : marsday_jxc

Target Server Type    : MYSQL
Target Server Version : 50561
File Encoding         : 65001

Date: 2019-02-08 16:55:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jxc_input
-- ----------------------------
DROP TABLE IF EXISTS `jxc_input`;
CREATE TABLE `jxc_input` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NOT NULL,
  `volume` int(11) unsigned zerofill NOT NULL,
  `price` int(11) unsigned zerofill NOT NULL,
  `buytime` date DEFAULT NULL,
  `recordtime` date DEFAULT NULL,
  `operator` varchar(255) DEFAULT '',
  `customer_info` varchar(512) DEFAULT '',
  `refer` varchar(512) DEFAULT '',
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='日常收入';

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='日常支出';

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='销售收入';

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

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
-- Table structure for jxc_output
-- ----------------------------
DROP TABLE IF EXISTS `jxc_output`;
CREATE TABLE `jxc_output` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NOT NULL,
  `volume` int(11) unsigned zerofill NOT NULL,
  `price` int(11) unsigned zerofill NOT NULL,
  `buytime` date DEFAULT NULL,
  `recordtime` date DEFAULT NULL,
  `operator` varchar(255) DEFAULT '',
  `customer_info` varchar(512) DEFAULT '',
  `refer` varchar(512) DEFAULT '',
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
