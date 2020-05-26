/*
 Target Server Type    : MySQL 5.7.28
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 26/05/2020 23:39:22
*/

CREATE DATABASE IF NOT EXISTS relics_manage_sys DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE relics_manage_sys;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job`
(
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '职务编号',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '职务名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '职务表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job
-- ----------------------------
INSERT INTO `job`
VALUES (1, '拍照人员');
INSERT INTO `job`
VALUES (2, '资产科');
INSERT INTO `job`
VALUES (3, '仓库管理员');
INSERT INTO `job`
VALUES (4, '文职人员');
INSERT INTO `job`
VALUES (5, '管理员');

-- ----------------------------
-- Table structure for job_permission
-- ----------------------------
DROP TABLE IF EXISTS `job_permission`;
CREATE TABLE `job_permission`
(
    `job_id` int(10) UNSIGNED NOT NULL COMMENT '职务编号',
    `permission_id` int(10) UNSIGNED NOT NULL COMMENT '权限编号',
    INDEX `job_permission_ibfk_job_id` (`job_id`) USING BTREE,
    INDEX `job_permission_ibfk_permission_id` (`permission_id`) USING BTREE,
    CONSTRAINT `job_permission_ibfk_job_id` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `job_permission_ibfk_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '职务基础权限表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job_permission
-- ----------------------------
INSERT INTO `job_permission`
VALUES (1, 3);
INSERT INTO `job_permission`
VALUES (2, 7);
INSERT INTO `job_permission`
VALUES (2, 4);
INSERT INTO `job_permission`
VALUES (3, 2);
INSERT INTO `job_permission`
VALUES (3, 6);
INSERT INTO `job_permission`
VALUES (3, 8);
INSERT INTO `job_permission`
VALUES (3, 9);
INSERT INTO `job_permission`
VALUES (3, 12);
INSERT INTO `job_permission`
VALUES (3, 4);
INSERT INTO `job_permission`
VALUES (4, 4);
INSERT INTO `job_permission`
VALUES (4, 5);
INSERT INTO `job_permission`
VALUES (4, 14);
INSERT INTO `job_permission`
VALUES (4, 11);
INSERT INTO `job_permission`
VALUES (5, 1);
INSERT INTO `job_permission`
VALUES (5, 2);
INSERT INTO `job_permission`
VALUES (5, 3);
INSERT INTO `job_permission`
VALUES (5, 4);
INSERT INTO `job_permission`
VALUES (5, 5);
INSERT INTO `job_permission`
VALUES (5, 6);
INSERT INTO `job_permission`
VALUES (5, 7);
INSERT INTO `job_permission`
VALUES (5, 8);
INSERT INTO `job_permission`
VALUES (5, 9);
INSERT INTO `job_permission`
VALUES (5, 11);
INSERT INTO `job_permission`
VALUES (5, 12);
INSERT INTO `job_permission`
VALUES (5, 13);
INSERT INTO `job_permission`
VALUES (5, 14);

-- ----------------------------
-- Table structure for operation
-- ----------------------------
DROP TABLE IF EXISTS `operation`;
CREATE TABLE `operation`
(
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '操作编号',
    `operator_id` int(11) UNSIGNED NOT NULL COMMENT '操作人id',
    `operate_item_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '被操作物id',
    `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型 user用户 relic文物 warehouse仓库',
    `detail_log` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详细信息',
    `create_time` datetime(0) NOT NULL COMMENT '操作时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `operation_ibfk_operator_id` (`operator_id`) USING BTREE,
    CONSTRAINT `operation_ibfk_operator_id` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '操作记录表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of operation
-- ----------------------------

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限编号',
    `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内部使用的代码',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 15
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '权限表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission`
VALUES (1, 'admin', '管理员');
INSERT INTO `permission`
VALUES (2, 'warehouse', '创建、修改、删除仓库');
INSERT INTO `permission`
VALUES (3, 'relic:add', '拍照创建文物');
INSERT INTO `permission`
VALUES (4, 'relic:info:view', '查看文物详细信息');
INSERT INTO `permission`
VALUES (5, 'relic:info:edit', '修改文物详细信息');
INSERT INTO `permission`
VALUES (6, 'relic:status:edit', '文物入库、外借、送修、离馆');
INSERT INTO `permission`
VALUES (7, 'relic:price', '查看、修改文物价值信息');
INSERT INTO `permission`
VALUES (8, 'relic:check', '盘点文物');
INSERT INTO `permission`
VALUES (9, 'relic:move', '移动文物');
INSERT INTO `permission`
VALUES (11, 'relic:export:relics', '查询、导出文物一览表');
INSERT INTO `permission`
VALUES (12, 'relic:export:warehouse', '查询、导出某仓库文物一览表');
INSERT INTO `permission`
VALUES (13, 'relic:export:changes', '查询、导出文物流水表');
INSERT INTO `permission`
VALUES (14, 'relic:status:enter', '文物入馆');

-- ----------------------------
-- Table structure for relic
-- ----------------------------
DROP TABLE IF EXISTS `relic`;
CREATE TABLE `relic`
(
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文物编号',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `quantity` int(11) NULL DEFAULT NULL COMMENT '数量',
    `picture_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '照片地址',
    `year` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年代',
    `reign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年号',
    `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '器型',
    `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源',
    `size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '尺寸',
    `weight` double(15, 2) NULL DEFAULT NULL COMMENT '重量 kg',
    `warehouse_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '收储仓库id',
    `shelf_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '收储货架id',
    `enter_price` decimal(15, 2) NULL DEFAULT NULL COMMENT '入馆价值',
    `leave_price` decimal(15, 2) NULL DEFAULT NULL COMMENT '离馆价值',
    `status_id` int(11) UNSIGNED NOT NULL COMMENT '状态id',
    `last_check_time` datetime(0) NULL DEFAULT NULL COMMENT '最后盘点时间',
    `enter_time` datetime(0) NULL DEFAULT NULL COMMENT '入馆时间',
    `leave_time` datetime(0) NULL DEFAULT NULL COMMENT '离馆时间',
    `move_time` datetime(0) NULL DEFAULT NULL COMMENT '移入仓库时间',
    `lend_time` datetime(0) NULL DEFAULT NULL COMMENT '出借时间',
    `fix_time` datetime(0) NULL DEFAULT NULL COMMENT '送修时间',
    `comment1` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注1',
    `comment2` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注2',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '更新时间/录入时间',
    `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `relic_ibfk_warehouse_id` (`warehouse_id`) USING BTREE,
    INDEX `relic_ibfk_status` (`status_id`) USING BTREE,
    INDEX `relic_idx_enter_time` (`enter_time`) USING BTREE,
    INDEX `relic_idx_leave_time` (`leave_time`) USING BTREE,
    INDEX `relic_idx_move_time` (`move_time`) USING BTREE,
    INDEX `relic_idx_lend_time` (`lend_time`) USING BTREE,
    INDEX `relic_idx_last_check_time` (`last_check_time`) USING BTREE,
    INDEX `relic_idx_fix_time` (`fix_time`) USING BTREE,
    INDEX `relic_idx_update_time` (`update_time`) USING BTREE,
    INDEX `relic_ibfk_shelf_id` (`shelf_id`) USING BTREE,
    CONSTRAINT `relic_ibfk_shelf_id` FOREIGN KEY (`shelf_id`) REFERENCES `shelf` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `relic_ibfk_status` FOREIGN KEY (`status_id`) REFERENCES `relic_status` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `relic_ibfk_warehouse_id` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '文物表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of relic
-- ----------------------------

-- ----------------------------
-- Table structure for relic_check
-- ----------------------------
DROP TABLE IF EXISTS `relic_check`;
CREATE TABLE `relic_check`
(
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '盘点编号',
    `warehouse_id` int(11) UNSIGNED NOT NULL COMMENT '盘点仓库编号',
    `start_time` datetime(0) NOT NULL COMMENT '盘点开始时间',
    `end_time` datetime(0) NULL DEFAULT NULL COMMENT '盘点结束时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `relic_check_ibfk_warehouse_id` (`warehouse_id`) USING BTREE,
    CONSTRAINT `relic_check_ibfk_warehouse_id` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '盘点表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of relic_check
-- ----------------------------

-- ----------------------------
-- Table structure for relic_check_detail
-- ----------------------------
DROP TABLE IF EXISTS `relic_check_detail`;
CREATE TABLE `relic_check_detail`
(
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增列',
    `check_id` int(11) UNSIGNED NOT NULL COMMENT '盘点编号',
    `relic_id` int(11) UNSIGNED NOT NULL COMMENT '文物编号',
    `old_warehouse_id` int(11) UNSIGNED NOT NULL COMMENT '盘点前文物所在仓库编号',
    `old_shelf_id` int(11) UNSIGNED NOT NULL COMMENT '盘点前文物所在货架编号',
    `new_warehouse_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '盘点后文物所在仓库编号',
    `new_shelf_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '盘点后文物所在货架编号',
    `operator_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '盘点人id',
    `check_time` datetime(0) NULL DEFAULT NULL COMMENT '盘点时间',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `relic_check_detail_ibfk_check_id` (`check_id`) USING BTREE,
    INDEX `relic_check_detail_ibfk_new_warehouse_id` (`new_warehouse_id`) USING BTREE,
    INDEX `relic_check_detail_ibfk_old_warehouse_id` (`old_warehouse_id`) USING BTREE,
    INDEX `relic_check_detail_ibfk_relic_id` (`relic_id`) USING BTREE,
    INDEX `relic_check_detail_ibfk_old_shelf_id` (`old_shelf_id`) USING BTREE,
    INDEX `relic_check_detail_ibfk_new_shelf_id` (`new_shelf_id`) USING BTREE,
    INDEX `relic_check_detail_ibfk_operator_id` (`operator_id`) USING BTREE,
    CONSTRAINT `relic_check_detail_ibfk_check_id` FOREIGN KEY (`check_id`) REFERENCES `relic_check` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `relic_check_detail_ibfk_new_shelf_id` FOREIGN KEY (`new_shelf_id`) REFERENCES `shelf` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `relic_check_detail_ibfk_new_warehouse_id` FOREIGN KEY (`new_warehouse_id`) REFERENCES `warehouse` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `relic_check_detail_ibfk_old_shelf_id` FOREIGN KEY (`old_shelf_id`) REFERENCES `shelf` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `relic_check_detail_ibfk_old_warehouse_id` FOREIGN KEY (`old_warehouse_id`) REFERENCES `warehouse` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `relic_check_detail_ibfk_operator_id` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `relic_check_detail_ibfk_relic_id` FOREIGN KEY (`relic_id`) REFERENCES `relic` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '盘点文物记录表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of relic_check_detail
-- ----------------------------

-- ----------------------------
-- Table structure for relic_status
-- ----------------------------
DROP TABLE IF EXISTS `relic_status`;
CREATE TABLE `relic_status`
(
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文物状态编号',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '文物状态表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of relic_status
-- ----------------------------
INSERT INTO `relic_status`
VALUES (1, '待评估');
INSERT INTO `relic_status`
VALUES (2, '在馆');
INSERT INTO `relic_status`
VALUES (3, '外借');
INSERT INTO `relic_status`
VALUES (4, '修理');
INSERT INTO `relic_status`
VALUES (5, '离馆');

-- ----------------------------
-- Table structure for shelf
-- ----------------------------
DROP TABLE IF EXISTS `shelf`;
CREATE TABLE `shelf`
(
    `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '货架编号',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '仓库名',
    `warehouse_id` int(11) UNSIGNED NOT NULL COMMENT '所属仓库编号',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `shelf_ibfk_warehouse_id` (`warehouse_id`) USING BTREE,
    CONSTRAINT `shelf_ibfk_warehouse_id` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '仓库表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of shelf
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户编号',
    `work_id` int(11) UNSIGNED NOT NULL COMMENT '工号',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
    `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
    `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码 盐',
    `job_id` int(255) UNSIGNED NOT NULL COMMENT '职务',
    `telephone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_idx_work_id` (`work_id`) USING BTREE COMMENT '工号唯一索引',
    INDEX `user_ibfk_job` (`job_id`) USING BTREE,
    INDEX `user_idx_work_id_password` (`work_id`, `password`) USING BTREE COMMENT '工号+密码',
    CONSTRAINT `user_ibfk_job` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for user_extra_permission
-- ----------------------------
DROP TABLE IF EXISTS `user_extra_permission`;
CREATE TABLE `user_extra_permission`
(
    `user_id` int(11) UNSIGNED NOT NULL COMMENT '用户编号',
    `permission_id` int(11) UNSIGNED NOT NULL COMMENT '权限编号',
    INDEX `user_permission_ibfk_user_id` (`user_id`) USING BTREE,
    INDEX `user_permission_ibfk_permission_id` (`permission_id`) USING BTREE,
    CONSTRAINT `user_extra_permission_ibfk_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `user_extra_permission_ibfk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户临时权限表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_extra_permission
-- ----------------------------

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse`
(
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '仓库编号',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '仓库名',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '仓库表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of warehouse
-- ----------------------------

-- ----------------------------
-- View structure for job_permission_view
-- ----------------------------
DROP VIEW IF EXISTS `job_permission_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `job_permission_view` AS
SELECT `job`.`id` AS `job_id`, `job`.`name` AS `job_name`, `permission`.`id` AS `permission_id`, `permission`.`code` AS `code`, `permission`.`name` AS `permission_name`
FROM ((`job` JOIN `job_permission`)
         JOIN `permission`)
WHERE ((`job`.`id` = `job_permission`.`job_id`) AND (`job_permission`.`permission_id` = `permission`.`id`))
ORDER BY `job`.`id`, `permission`.`id`;

-- ----------------------------
-- View structure for user_extra_permission_view
-- ----------------------------
DROP VIEW IF EXISTS `user_extra_permission_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `user_extra_permission_view` AS
SELECT `user`.`id` AS `user_id`,
       `user`.`work_id` AS `work_id`,
       `user`.`name` AS `user_name`,
       `job`.`id` AS `job_id`,
       `job`.`name` AS `job_name`,
       `permission`.`id` AS `permission_id`,
       `permission`.`code` AS `code`,
       `permission`.`name` AS `permission_name`
FROM (((`job` JOIN `permission`) JOIN `user_extra_permission`)
         JOIN `user`)
WHERE ((`user`.`id` = `user_extra_permission`.`user_id`) AND (`user_extra_permission`.`permission_id` = `permission`.`id`) AND (`job`.`id` = `user`.`job_id`))
ORDER BY `user`.`id`, `permission`.`id`;

SET FOREIGN_KEY_CHECKS = 1;
