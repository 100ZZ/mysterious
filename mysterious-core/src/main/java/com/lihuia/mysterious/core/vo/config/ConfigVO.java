package com.lihuia.mysterious.core.vo.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/4/1 上午9:35
 */

@Data
@ApiModel
public class ConfigVO {

    @ApiModelProperty(value = "配置编号")
    private Long id;

    @ApiModelProperty(value = "配置字段", required = true)
    private String key;

    @ApiModelProperty(value = "字段值", required = true)
    private String value;

    @ApiModelProperty("字段描述")
    private String description;
}
