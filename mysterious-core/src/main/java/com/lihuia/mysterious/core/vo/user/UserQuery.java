package com.lihuia.mysterious.core.vo.user;

import com.lihuia.mysterious.core.vo.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午2:33
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends BaseQuery {

    @ApiModelProperty(value = "用户名称")
    private String username;
}
