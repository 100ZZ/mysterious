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
public class ThreadGroupVO extends BaseVO<Long> {

    /** 关联的用例 */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /** 关联的脚本ID */
    @ApiModelProperty(value = "关联的脚本ID")
    private Long jmxId;

    /** 线程数 */
    @ApiModelProperty(value = "线程数")
    private String numThreads;

    /** 持续时间 */
    @ApiModelProperty(value = "持续时间")
    private String rampTime;

    /** 循环次数 */
    @ApiModelProperty(value = "循环次数")
    private String loops;

    /** 默认值:1;每次迭代是否使用相同线程 */
    @ApiModelProperty(value = "默认值:1;每次迭代是否使用相同线程")
    private Integer sameUserOnNextIteration;

    /** 默认值:0;是否需要的时候创建线程 */
    @ApiModelProperty(value = "默认值:0;是否需要的时候创建线程")
    private Integer delayedStart;

    /** 默认值:1;是否使用调度器 */
    @ApiModelProperty(value = "默认值:1;是否使用调度器")
    private Integer scheduler;

    /** 线程组运行时间 */
    @ApiModelProperty(value = "线程组运行时间")
    private String duration;

    /** 线程启动延迟时间 */
    @ApiModelProperty(value = "线程启动延迟时间")
    private String delay;
}
