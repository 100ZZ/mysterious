package com.lihuia.mysterious.core.entity.node;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2023/3/27 8:11 PM
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class NodeDO extends BaseDO<Long> {

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

    /** ssh密码 */
    private String password;

    /** ssh端口 */
    private Integer port;

    /** 节点状态 0-禁用中，1-启用中 */
    private Integer status;
}
