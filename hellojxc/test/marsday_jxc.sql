/*
Navicat MySQL Data Transfer

Source Server         : 本地mysql
Source Server Version : 50561
Source Host           : localhost:3306
Source Database       : marsday_jxc

Target Server Type    : MYSQL
Target Server Version : 50561
File Encoding         : 65001

Date: 2019-01-03 22:55:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jxc_customer
-- ----------------------------
DROP TABLE IF EXISTS `jxc_customer`;
CREATE TABLE `jxc_customer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `type` tinyint(1) DEFAULT '0',
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jxc_customer
-- ----------------------------

-- ----------------------------
-- Table structure for jxc_goods
-- ----------------------------
DROP TABLE IF EXISTS `jxc_goods`;
CREATE TABLE `jxc_goods` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` tinyint(1) DEFAULT '0',
  `del_flag` tinyint(1) unsigned zerofill DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jxc_goods
-- ----------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jxc_input
-- ----------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jxc_output
-- ----------------------------

-- ----------------------------
-- Table structure for jxc_user
-- ----------------------------
DROP TABLE IF EXISTS `jxc_user`;
CREATE TABLE `jxc_user` (
  `name_en` varchar(255) NOT NULL,
  `name_ch` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `last_login` varchar(255) DEFAULT '',
  `del_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`name_en`,`name_ch`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jxc_user
-- ----------------------------
INSERT INTO `jxc_user` VALUES ('xiaoli', '小李', '123456', '', '0');
INSERT INTO `jxc_user` VALUES ('xiaoma', '小马', '123456', '', '0');
