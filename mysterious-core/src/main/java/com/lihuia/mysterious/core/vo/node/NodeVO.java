package com.lihuia.mysterious.core.vo.node;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author maple@lihuia.com
 * @date 2023/3/29 10:08 PM
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class NodeVO extends BaseVO<Long> {

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

    @ApiModelProperty(value = "节点状态 0-禁用中，1-启用中")
    private Integer status;
}
