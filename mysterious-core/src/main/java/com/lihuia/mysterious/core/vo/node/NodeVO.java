package com.lihuia.mysterious.core.vo.node;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:08 PM
 */

@Data
public class NodeVO {

    @ApiModelProperty("节点编号")
    private Long id;

    @ApiModelProperty("节点名称")
    private String name;

    @ApiModelProperty("节点描述")
    private String description;

    @ApiModelProperty("节点类型")
    private Integer type;

    @ApiModelProperty("节点IP地址")
    private String host;

    @ApiModelProperty("ssh用户")
    private String username;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("ssh端口")
    private Integer port;

    @ApiModelProperty("节点状态 0-禁用中，1-启用中")
    private Integer status;
}
