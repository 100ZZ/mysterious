package com.lihuia.mysterious.core.vo.jmx.sample.java;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
@ApiModel
public class JavaParamVO {

    /** 名称 */
    @ApiModelProperty(value = "名称")
    private String paramKey;

    /** 值 */
    @ApiModelProperty(value = "值")
    private String paramValue;
}
