package com.lihuia.mysterious.core.vo.config;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2023/4/1 上午9:35
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class ConfigVO extends BaseVO<Long> {

    @ApiModelProperty(value = "配置字段")
    private String configKey;

    @ApiModelProperty(value = "字段值")
    private String configValue;

    @ApiModelProperty("字段描述")
    private String description;
}
