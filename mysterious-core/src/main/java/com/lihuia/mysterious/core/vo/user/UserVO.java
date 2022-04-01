package com.lihuia.mysterious.core.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:38 PM
 */

@Data
@Builder
public class UserVO {

    @ApiModelProperty("用户编号")
    private Long id;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;
}
