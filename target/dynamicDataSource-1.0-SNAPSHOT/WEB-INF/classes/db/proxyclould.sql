/*
Navicat MySQL Data Transfer

Source Server         : SK_MySQL
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : proxyclould

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-12-05 17:50:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tenant`
-- ----------------------------
DROP TABLE IF EXISTS `tenant`;
CREATE TABLE `tenant` (
  `tenantID` varchar(30) NOT NULL,
  `jdbc_Url` varchar(1000) NOT NULL,
  `date` varchar(30) NOT NULL,
  `type` varchar(200) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`tenantID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tenant
-- ----------------------------
INSERT INTO `tenant` VALUES ('sk', 'jdbc:log4jdbc:mysql://localhost:3306/sk?useUnicode=true&characterEncoding=gbk&zeroDateTimeBehavior=convertToNull', '2018-12-05 08:45:45', null, 'root', '123456');
INSERT INTO `tenant` VALUES ('sk1', 'jdbc:log4jdbc:mysql://localhost:3306/sk1?useUnicode=true&characterEncoding=gbk&zeroDateTimeBehavior=convertToNull', '2018-12-05 08:46:15', null, 'root', '123456');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'tom', '123456');
INSERT INTO `t_user` VALUES ('2', 'jack', '123456');
INSERT INTO `t_user` VALUES ('3', 'rose', '123456');
