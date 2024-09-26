package com.lihuia.mysterious.core.vo.jmx.sample.assertion;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2024/9/23 09:37
 */

@Data
@ApiModel
public class JsonAssertionVO {

    /** 断言的JSON路径 */
    @ApiModelProperty(value = "断言的JSON路径")
    private String jsonPath;

    /** 预期值 */
    @ApiModelProperty(value = "预期值")
    private String expectedValue;
}
