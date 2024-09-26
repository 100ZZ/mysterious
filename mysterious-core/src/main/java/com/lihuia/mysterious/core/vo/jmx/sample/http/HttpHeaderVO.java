package com.lihuia.mysterious.core.vo.jmx.sample.http;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class HttpHeaderVO extends BaseVO<Long> {

    /** 关联的用例 */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /** 关联jmxDO */
    @ApiModelProperty(value = "关联jmxDO")
    private Long jmxId;

    /** 关联HttpDO */
    @ApiModelProperty(value = "关联HttpDO")
    private Long httpId;

    /** 字段 */
    @ApiModelProperty(value = "字段")
    private String headerKey;

    /** 字段的值 */
    @ApiModelProperty(value = "字段的值")
    private String headerValue;
}
