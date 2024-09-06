package com.lihuia.mysterious.core.entity.jmx.thread;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
public class ConcurrencyThreadGroupDO extends BaseDO<Long> {

    private static final long serialVersionUID = 31234592228884767L;

    /** 关联的用例 */
    private Long testCaseId;

    /** 关联的脚本ID */
    private Long jmxId;

    /** 目标线程数 */
    private String targetConcurrency;

    /** 梯度时间 */
    private String rampUpTime;

    /** 梯度数量 */
    private String rampUpStepsCount;

    /** 梯度结束后持续时间 */
    private String holdTargetRateTime;
}
