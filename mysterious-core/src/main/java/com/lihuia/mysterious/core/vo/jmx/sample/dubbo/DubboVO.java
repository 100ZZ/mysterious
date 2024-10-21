package com.lihuia.mysterious.core.vo.jmx.sample.dubbo;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class DubboVO extends BaseVO<Long> {

    /** 关联的用例 */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /** 关联的JMX脚本 */
    @ApiModelProperty(value = "关联的JMX脚本")
    private Long jmxId;

    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "接口描述")
    private String comments;

    /** 配置中心协议 */
    @ApiModelProperty(value = "配置中心协议")
    private String configCenterProtocol;

    /** 配置中心组 */
    @ApiModelProperty(value = "配置中心组")
    private String configCenterGroup;

    /** 配置中心命名空间 */
    @ApiModelProperty(value = "配置中心命名空间")
    private String configCenterNamespace;

    /** 配置中心用户名 */
    @ApiModelProperty(value = "配置中心用户名")
    private String configCenterUsername;

    /** 配置中心密码 */
    @ApiModelProperty(value = "配置中心密码")
    private String configCenterPassword;

    /** 配置中心地址 */
    @ApiModelProperty(value = "配置中心地址")
    private String configCenterAddress;

    /** 配置中心超时时间 */
    @ApiModelProperty(value = "配置中心超时时间")
    private String configCenterTimeout;

    /** 注册中心协议 */
    @ApiModelProperty(value = "注册中心协议")
    private String registryProtocol;

    /** 注册中心组 */
    @ApiModelProperty(value = "注册中心组")
    private String registryGroup;

    /** 注册中心用户名 */
    @ApiModelProperty(value = "注册中心用户名")
    private String registryUsername;

    /** 注册中心密码 */
    @ApiModelProperty(value = "注册中心密码")
    private String registryPassword;

    /** 注册中心地址 */
    @ApiModelProperty(value = "注册中心地址")
    private String registryAddress;

    /** 注册中心超时时间 */
    @ApiModelProperty(value = "注册中心超时时间")
    private String registryTimeout;

    /** RPC协议 */
    @ApiModelProperty(value = "RPC协议")
    private String rpcProtocol;

    /** 超时时间 */
    @ApiModelProperty(value = "超时时间")
    private String timeout;

    /** 版本 */
    @ApiModelProperty(value = "版本")
    private String version;

    /** 重试次数 */
    @ApiModelProperty(value = "重试次数")
    private String retries;

    /** 集群策略 */
    @ApiModelProperty(value = "集群策略")
    private String cluster;

    /** 组 */
    @ApiModelProperty(value = "组")
    private String group;

    /** 连接数 */
    @ApiModelProperty(value = "连接数")
    private String connections;

    /** 异步/同步 */
    @ApiModelProperty(value = "异步/同步")
    private String async;

    /** 负载均衡策略 */
    @ApiModelProperty(value = "负载均衡策略")
    private String loadBalance;

    /** 接口 */
    @ApiModelProperty(value = "接口")
    private String interfaceName;

    /** 方法 */
    @ApiModelProperty(value = "方法")
    private String method;

    /** 方法参数数量 */
    @ApiModelProperty(value = "方法参数数量")
    private Integer methodArgsSize;

    /** 附件参数数量 */
    @ApiModelProperty(value = "附件参数数量")
    private Integer attachmentArgsSize;

    /** 方法参数列表 */
    @ApiModelProperty(value = "方法参数列表")
    private List<DubboMethodArgsVO> dubboMethodArgsVOList;

    /** 附件参数列表 */
    @ApiModelProperty(value = "附件参数列表")
    private List<DubboAttachmentArgsVO> dubboAttachmentArgsVOList;
}