/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50734 (5.7.34)
 Source Host           : localhost:3306
 Source Schema         : bms

 Target Server Type    : MySQL
 Target Server Version : 50734 (5.7.34)
 File Encoding         : 65001

 Date: 19/05/2025 20:43:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for battery_signal
-- ----------------------------
DROP TABLE IF EXISTS `battery_signal`;
CREATE TABLE `battery_signal`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `car_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车架编号',
  `statue` tinyint(1) NOT NULL COMMENT '处理状态 0-未处理 1-已处理',
  `signal_data` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始信号数据(JSON格式)',
  `signal_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '信号时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_car_time`(`car_id`, `signal_time`) USING BTREE COMMENT '快速查询特定车辆的最新信号',
  INDEX `idx_signal_time`(`signal_time`) USING BTREE COMMENT '查询特定时间范围的信号'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电池信号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of battery_signal
-- ----------------------------
INSERT INTO `battery_signal` VALUES (1, '123456', 1, '{\"speed\":80.5,\"batteryLevel\":75.2,\"temperature\":25.0}', '2025-05-19 20:30:08');
INSERT INTO `battery_signal` VALUES (2, '123456', 1, '{\"speed\":80.5,\"batteryLevel\":75.2,\"temperature\":25.0}', '2025-05-19 20:39:39');
INSERT INTO `battery_signal` VALUES (3, '123456', 1, '{\"speed\":80.5,\"batteryLevel\":75.2,\"temperature\":25.0}', '2025-05-19 20:39:40');

SET FOREIGN_KEY_CHECKS = 1;
