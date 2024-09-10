package com.lihuia.mysterious.core.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2023/3/29 10:38 PM
 */

@Data
@Builder
@ApiModel
public class UserParam {

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String realName;
}
