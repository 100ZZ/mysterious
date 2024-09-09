package com.lihuia.mysterious.core.vo.jmx.thread;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
public class SteppingThreadGroupVO extends BaseVO<Long> {

    /** 关联的用例 */
    private Long testCaseId;

    /** 关联的脚本ID */
    private Long jmxId;

    /** 脚本里：ThreadGroup.num_threads */
    private String numThreads = "2";

    /** 脚本里：Threads initial delay */
    private String firstWaitForSeconds = "0";

    /** 脚本里：Start users count burst */
    private String thenStartThreads = "1";

    /** 脚本里：Start users count */
    private String nextAddThreads = "1";

    /** 脚本里：Start users period */
    private String nextAddThreadsEverySeconds = "1";

    /** 脚本里：rampUp */
    private String usingRampUpSeconds = "0";

    /** 脚本里：flighttime */
    private String thenHoldLoadForSeconds = "1";

    /** 脚本里：Stop users count */
    private String finallyStopThreads = "1";

    /** 脚本里：Stop users period */
    private String finallyStopThreadsEverySeconds = "0";
}
