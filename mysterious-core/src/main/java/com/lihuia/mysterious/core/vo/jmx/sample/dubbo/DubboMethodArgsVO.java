package com.lihuia.mysterious.core.vo.jmx.sample.dubbo;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2024/10/21 11:29
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class DubboMethodArgsVO extends BaseVO<Long> {

    /** 关联的用例 */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /** 关联的JMX脚本 */
    @ApiModelProperty(value = "关联的JMX脚本")
    private Long jmxId;

    /** 关联的Dubbo请求 */
    @ApiModelProperty(value = "关联的Dubbo请求")
    private Long dubboId;

    /** 参数类型 */
    @ApiModelProperty(value = "参数类型")
    private String paramType;

    /** 参数值 */
    @ApiModelProperty(value = "参数值")
    private String paramValue;
}
