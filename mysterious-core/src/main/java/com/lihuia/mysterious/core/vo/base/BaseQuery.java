package com.lihuia.mysterious.core.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午2:31
 */

@Data
@ApiModel
public class BaseQuery implements Serializable {

    private static final long serialVersionUID = -6571908061802253006L;

    @ApiModelProperty(value = "页码 第一页:1", required = true)
    private Integer page;

    @ApiModelProperty(value = "每页数量", required = true)
    private Integer size;
}
