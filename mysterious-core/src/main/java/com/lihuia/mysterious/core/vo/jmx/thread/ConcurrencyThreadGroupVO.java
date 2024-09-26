package com.lihuia.mysterious.core.vo.jmx.thread;

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
public class ConcurrencyThreadGroupVO extends BaseVO<Long> {

    /** 关联的用例 */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /** 关联的脚本ID */
    @ApiModelProperty(value = "关联的脚本ID")
    private Long jmxId;

    /** 目标线程数 */
    @ApiModelProperty(value = "目标线程数")
    private String targetConcurrency;

    /** 梯度时间 */
    @ApiModelProperty(value = "梯度时间")
    private String rampUpTime;

    /** 梯度数量 */
    @ApiModelProperty(value = "梯度数量")
    private String rampUpStepsCount;

    /** 梯度结束后持续时间 */
    @ApiModelProperty(value = "梯度结束后持续时间")
    private String holdTargetRateTime;
}
