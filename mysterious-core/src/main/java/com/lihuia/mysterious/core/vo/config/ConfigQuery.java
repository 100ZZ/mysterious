package com.lihuia.mysterious.core.vo.config;

import com.lihuia.mysterious.core.vo.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午2:58
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class ConfigQuery extends BaseQuery {

    @ApiModelProperty(value = "配置字段")
    private String configKey;
}
