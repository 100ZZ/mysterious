package com.lihuia.mysterious.core.vo.node;

import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:08 PM
 */

@Data
public class NodeVO {

    private Long id;

    /** 节点名称 */
    private String name;

    /** 节点描述 */
    private String description;

    /** 节点类型 */
    private Integer type;

    /** 节点IP地址 */
    private String host;

    /** ssh用户 */
    private String username;

    /** 登录密码 */
    private String password;

    /** ssh端口 */
    private Integer port;

    /** 节点状态 0-禁用中，1-启用中 */
    private Integer status;
}
