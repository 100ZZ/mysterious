package com.lihuia.mysterious.core.entity.report;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author maple@lihuia.com
 * @date 2023/4/1 下午4:41
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportDO extends BaseDO<Long> {

    /** 报告名称 */
    private String name;

    /** 报告描述 */
    private String description;

    /** 用例ID */
    private Long testCaseId;

    /** 报告路径 */
    private String reportDir;

    /** 执行类型，1-调试；2-执行 */
    private Integer execType;

    /** 执行状态; 0-未执行，1-执行中, 2-执行成功, 3-执行异常 */
    private Integer status;

    /** 调试的返回值 */
    private String responseData;

    /** jmeter log path */
    private String jmeterLogFilePath;
}
