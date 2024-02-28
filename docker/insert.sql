-- master节点用例，报告目录
INSERT INTO mysterious_config (config_key, config_value, description) VALUES ("MASTER_DATA_HOME", "/opt/mysterious/mysterious-data", "master节点用例，报告目录");
-- master节点Jmeter路径
INSERT INTO mysterious_config (config_key, config_value, description) VALUES ("MASTER_JMETER_HOME", "/opt/mysterious/mysterious-jmeter", "master节点Jmeter路径");
-- master节点Jmeter执行目录
INSERT INTO mysterious_config (config_key, config_value, description) VALUES ("MASTER_JMETER_BIN_HOME", "/opt/mysterious/mysterious-jmeter/bin", "master节点Jmeter执行目录");
-- slave节点Jmeter-Server执行目录
INSERT INTO mysterious_config (config_key, config_value, description) VALUES ("SLAVE_JMETER_BIN_HOME", "/opt/mysterious/mysterious-jmeter/bin", "slave节点Jmeter-Server执行目录");
-- slave节点Jmeter-Server日志目录
INSERT INTO mysterious_config (config_key, config_value, description) VALUES ("SLAVE_JMETER_LOG_HOME", "/opt/mysterious/mysterious-jmeter/log", "slave节点Jmeter-Server日志目录");
-- master节点host
INSERT INTO mysterious_config (config_key, config_value, description) VALUES ("MASTER_HOST_PORT", "192.168.20.224:9998", "压测报告预览链接的Host");

