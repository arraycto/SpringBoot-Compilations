/*
 Navicat Premium Data Transfer

 Source Server         : root root
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : iot_v1

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 09/10/2019 17:53:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(2048) CHARACTER SET utf8 NOT NULL,
  `create_time` varchar(255) CHARACTER SET utf8 NOT NULL,
  `last_modify_time` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `rule_key` varchar(255) CHARACTER SET utf8 NOT NULL,
  `version` varchar(255) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9yepjak9olg92holwkr8p3l0f` (`rule_key`),
  UNIQUE KEY `UK_ilmbp99kyt6gy10224pc9bl6n` (`version`),
  UNIQUE KEY `UK_ei48upwykmhx9r5p7p4ndxvgn` (`last_modify_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of rule
-- ----------------------------
BEGIN;
INSERT INTO `rule` VALUES (1, 'package com.wjwcloud.drools\n\nimport com.wjwcloud.drools.model.Address;\nimport com.wjwcloud.drools.model.fact.AddressCheckResult;\n\nrule \"Postcode should be filled with exactly 5 numbers\"\n    when\n        address : Address(postcode != null)\n        checkResult : AddressCheckResult();\n    then\n        checkResult.setPostCodeResult(true);\n		System.out.println(\"规则中打印日志：校验通过!\");\nend', '111', '111', 'score', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
