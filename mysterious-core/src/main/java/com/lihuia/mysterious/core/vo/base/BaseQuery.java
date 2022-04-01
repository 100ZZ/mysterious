package com.lihuia.mysterious.core.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午2:31
 */

@Data
@ApiModel
public class BaseQuery implements Serializable {

    private static final long serialVersionUID = -6571908061802253006L;

    @ApiModelProperty("页码 第一页:1")
    private Integer page;

    @ApiModelProperty("每页数量")
    private Integer size;
}
