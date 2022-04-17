CREATE TABLE `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` varchar(128) NOT NULL DEFAULT '' COMMENT '节点登陆用户名',
    `password` varchar(128) NOT NULL DEFAULT '' COMMENT '节点登陆密码',
    `token` varchar(128) NOT NULL DEFAULT '' COMMENT 'token',
    `effect_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生效时间',
    `expire_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失效时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='用户信息表';

CREATE TABLE `config` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `config_key` varchar(255) NOT NULL DEFAULT '' COMMENT '配置字段',
    `config_value` varchar(255) NOT NULL DEFAULT '' COMMENT '配置值',
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT '配置描述',
    `creator_id` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
    `modifier_id` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人ID',
    `modifier` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='配置表';

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
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='分布式节点表';

CREATE TABLE `testcase` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(255) NOT NULL DEFAULT '' COMMENT '用例名称',
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT '用例描述',
    `biz` varchar(128) NOT NULL DEFAULT '' COMMENT '业务线',
    `service` varchar(128) NOT NULL DEFAULT '' COMMENT '服务名称',
    `version` varchar(128) NOT NULL DEFAULT '' COMMENT '服务版本',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0-未执行，1-执行中, 2-执行成功, 3-执行异常',
    `test_case_dir` varchar(255) NOT NULL DEFAULT '' COMMENT '用例目录',
    `creator_id` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
    `modifier_id` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人ID',
    `modifier` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='用例表';

CREATE TABLE `jmx` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `src_name` varchar(255) NOT NULL DEFAULT '' COMMENT '上传前脚本名称',
    `dst_name` varchar(255) NOT NULL DEFAULT '' COMMENT '上传后脚本名称',
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT '脚本描述',
    `jmx_dir` varchar(255) NOT NULL DEFAULT '' COMMENT '脚本目录',
    `test_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用例ID',
    `jmeter_script_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '脚本生成方式',
    `creator_id` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
    `modifier_id` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人ID',
    `modifier` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_test_case_id` (`test_case_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='JMX脚本表';

CREATE TABLE `jar` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `src_name` varchar(255) NOT NULL DEFAULT '' COMMENT '上传前JAR包名称',
    `dst_name` varchar(255) NOT NULL DEFAULT '' COMMENT '上传后JAR包名称',
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT 'JAR包描述',
    `node_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '节点ID',
    `jar_dir` varchar(255) NOT NULL DEFAULT '' COMMENT 'jar包目录',
    `test_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用例ID',
    `creator_id` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
    `modifier_id` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人ID',
    `modifier` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_test_case_id` (`test_case_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='JAR包表';

CREATE TABLE `csv` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `src_name` varchar(255) NOT NULL DEFAULT '' COMMENT '上传前CSV文件名称',
    `dst_name` varchar(255) NOT NULL DEFAULT '' COMMENT '上传后CSV文件名称',
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT 'CSV文件描述',
    `node_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '节点ID',
    `csv_dir` varchar(255) NOT NULL DEFAULT '' COMMENT 'csv文件目录',
    `test_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用例ID',
    `creator_id` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
    `modifier_id` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人ID',
    `modifier` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_test_case_id` (`test_case_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='CSV文件表';

CREATE TABLE `report` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(255) NOT NULL DEFAULT '' COMMENT '报告名称',
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT '报告描述',
    `test_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用例ID',
    `report_dir` varchar(255) NOT NULL DEFAULT '' COMMENT '测试报告目录',
    `exec_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '1-调试, 2-执行',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0-未执行，1-执行中, 2-执行成功, 3-执行异常',
    `response_data` varchar(512) NOT NULL DEFAULT '' COMMENT '调试结果',
    `jmeterLogFilePath` varchar(255) NOT NULL DEFAULT '' COMMENT '结果JTL文件',
    `creator_id` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
    `modifier_id` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人ID',
    `modifier` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_test_case_id` (`test_case_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='测试报告表';