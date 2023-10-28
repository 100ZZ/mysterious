package com.lihuia.mysterious.core.vo.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author maple@lihuia.com
 * @date 2023/3/29 10:08 PM
 */

@Data
@ApiModel
public class NodeParam {

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "节点描述")
    private String description;

    @ApiModelProperty(value = "节点类型")
    private Integer type;

    @ApiModelProperty(value = "节点IP地址")
    private String host;

    @ApiModelProperty(value = "ssh用户")
    private String username;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "ssh端口")
    private Integer port;
}
