package com.lihuia.mysterious.core.vo.jmx.sample.java;

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
public class JavaParamVO extends BaseVO<Long> {

    /** 关联的用例 */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /** 关联的脚本 */
    @ApiModelProperty(value = "关联的脚本")
    private Long jmxId;

    /** 关联的JavaDO */
    @ApiModelProperty(value = "关联的JavaDO")
    private Long javaId;

    /** 名称 */
    @ApiModelProperty(value = "名称")
    private String paramKey;

    /** 值 */
    @ApiModelProperty(value = "值")
    private String paramValue;
}
