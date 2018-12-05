/*
Navicat MySQL Data Transfer

Source Server         : SK_MySQL
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : sk

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-12-05 17:49:03
*/

SET FOREIGN_KEY_CHECKS=0;

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
INSERT INTO `t_user` VALUES ('1', 'tom_sk', '123456');
INSERT INTO `t_user` VALUES ('2', 'jack_sk', '123456');
INSERT INTO `t_user` VALUES ('3', 'rose_sk', '123456');
