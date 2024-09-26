package com.lihuia.mysterious.core.vo.jmx.sample.assertion;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2024/9/20 17:24
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class AssertionVO extends BaseVO<Long> {

    /** 关联的用例 */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /** 关联jmxDO */
    @ApiModelProperty(value = "关联jmxDO")
    private Long jmxId;

    /** 断言response code */
    @ApiModelProperty(value = "断言response code")
    private String responseCode;

    /** 断言response text */
    @ApiModelProperty(value = "断言response text")
    private String responseMessage;

    /** 断言JSON data */
    //@ApiModelProperty(value = "断言JSON data")
    //private List<JsonAssertionVO> jsonPathData;

    /** 断言的JSON路径 */
    private String jsonPath;

    /** 预期值 */
    private String expectedValue;
}
