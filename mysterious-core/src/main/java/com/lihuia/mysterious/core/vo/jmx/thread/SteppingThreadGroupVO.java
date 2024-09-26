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
public class SteppingThreadGroupVO extends BaseVO<Long> {

    /** 关联的用例 */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /** 关联的脚本ID */
    @ApiModelProperty(value = "关联的脚本ID")
    private Long jmxId;

    /** 脚本里：ThreadGroup.num_threads */
    @ApiModelProperty(value = "ThreadGroup.num_threads")
    private String numThreads = "2";

    /** 脚本里：Threads initial delay */
    @ApiModelProperty(value = "Threads initial delay")
    private String firstWaitForSeconds = "0";

    /** 脚本里：Start users count burst */
    @ApiModelProperty(value = "Start users count burst")
    private String thenStartThreads = "1";

    /** 脚本里：Start users count */
    @ApiModelProperty(value = "Start users count")
    private String nextAddThreads = "1";

    /** 脚本里：Start users period */
    @ApiModelProperty(value = "Start users period")
    private String nextAddThreadsEverySeconds = "1";

    /** 脚本里：rampUp */
    @ApiModelProperty(value = "rampUp")
    private String usingRampUpSeconds = "0";

    /** 脚本里：flighttime */
    @ApiModelProperty(value = "flighttime")
    private String thenHoldLoadForSeconds = "1";

    /** 脚本里：Stop users count */
    @ApiModelProperty(value = "Stop users count")
    private String finallyStopThreads = "1";

    /** 脚本里：Stop users period */
    @ApiModelProperty(value = "Stop users period")
    private String finallyStopThreadsEverySeconds = "0";
}
