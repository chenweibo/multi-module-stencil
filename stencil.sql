/*
 Navicat Premium Data Transfer

 Source Server         : my
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : stencil

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 26/08/2020 15:03:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `icon` varchar(500) DEFAULT NULL COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(200) DEFAULT NULL COMMENT '昵称',
  `note` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` int(1) DEFAULT '1' COMMENT '帐号启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='后台用户表';

-- ----------------------------
-- Records of admin
-- ----------------------------
BEGIN;
INSERT INTO `admin` VALUES (1, '62786084', '$2a$10$rQWNW3GUlIEZtnfYzU53O.rVhl7dpBtfXaCF9l5sdgUPpfzE5pOnq', NULL, NULL, NULL, NULL, '2020-07-10 13:50:48', NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) NOT NULL DEFAULT '' COMMENT '用户名',
  `email` varchar(255) DEFAULT '' COMMENT '邮箱',
  `password` varchar(255) DEFAULT '' COMMENT '密码',
  `telephone` int(15) DEFAULT NULL COMMENT '电话号码',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(1) DEFAULT NULL COMMENT '帐号启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES (1, '123456', '', '$2a$10$8phGvWZFMCHlzi4N4ygSseAOwinYPsq0o1Ot5/rqj9mpOKbaygDeS', NULL, '2020-07-11 14:37:27', 1);
INSERT INTO `users` VALUES (2, '', '', '$2a$10$XIlov1kESsXnC.vkIjTig.metqxhH/zGM68yxein7wBvXG10BnkH6', 123213, '2020-07-13 16:08:50', 1);
COMMIT;

-- ----------------------------
-- Table structure for users_platform
-- ----------------------------
DROP TABLE IF EXISTS `users_platform`;
CREATE TABLE `users_platform` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `users_id` int(11) DEFAULT NULL COMMENT '用户id',
  `platform_id` varchar(60) DEFAULT NULL COMMENT '平台id',
  `platform_token` varchar(11) DEFAULT NULL COMMENT '平台access_token',
  `type` int(5) DEFAULT NULL COMMENT '平台类型 0:未知,1:facebook,2:google,3:wechat,4:qq,5:weibo,6:twitter',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方登录表';

-- ----------------------------
-- Records of users_platform
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
