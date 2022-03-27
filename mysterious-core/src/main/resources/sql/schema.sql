CREATE TABLE `node` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(255) NOT NULL DEFAULT '' COMMENT '节点名称',
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT '节点描述',
    `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0-slave，1-master',
    `host` varchar(128) NOT NULL DEFAULT '' COMMENT '节点IP地址',
    `username` varchar(128) NOT NULL DEFAULT '' COMMENT '节点登陆用户名',
    `password` varchar(128) NOT NULL DEFAULT '' COMMENT '节点登陆密码',
    `port` bigint(20) NOT NULL DEFAULT '0' COMMENT '节点登陆端口',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0-禁用中，1-启用中',
    `creator_id` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
    `modifier_id` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人ID',
    `modifier` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
    `create_time` bigint(13) unsigned NOT NULL DEFAULT '0' COMMENT '生成时间',
    `modify_time` bigint(13) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='分布式节点表';