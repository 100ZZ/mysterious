package com.lihuia.mysterious.core.entity.jmx.sample.dubbo;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
public class DubboDO extends BaseDO<Long> {

    /** 用例ID */
    private Long testCaseId;

    /** 脚本ID */
    private Long jmxId;

    private String name;

    private String comments;

    /** 配置中心协议 */
    private String configCenterProtocol;

    /** 配置中心组 */
    private String configCenterGroup;

    /** 配置中心命名空间 */
    private String configCenterNamespace;

    /** 配置中心用户名 */
    private String configCenterUsername;

    /** 配置中心密码 */
    private String configCenterPassword;

    /** 配置中心地址 */
    private String configCenterAddress;

    /** 配置中心超时时间 */
    private String configCenterTimeout;

    /** 注册中心协议 */
    private String registryProtocol;

    /** 注册中心组 */
    private String registryGroup;

    /** 注册中心用户名 */
    private String registryUsername;

    /** 注册中心密码 */
    private String registryPassword;

    /** 注册中心地址 */
    private String registryAddress;

    /** 注册中心超时时间 */
    private String registryTimeout;

    /** RPC协议 */
    private String rpcProtocol;

    /** 超时时间 */
    private String timeout;

    /** 版本 */
    private String version;

    /** 重试次数 */
    private String retries;

    /** 集群策略 */
    private String cluster;

    /** 组 */
    private String group;

    /** 连接数 */
    private String connections;

    /** 异步/同步 */
    private String async;

    /** 负载均衡策略 */
    private String loadBalance;

    /** 接口 */
    private String interfaceName;

    /** 方法 */
    private String method;

    /** 方法参数数量 */
    private Integer methodArgsSize;

    /** 附件参数数量 */
    private Integer attachmentArgsSize;
}
