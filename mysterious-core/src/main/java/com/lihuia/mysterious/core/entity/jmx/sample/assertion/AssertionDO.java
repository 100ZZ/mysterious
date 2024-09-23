package com.lihuia.mysterious.core.entity.jmx.sample.assertion;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/9/23 09:47
 */

@Data
public class AssertionDO extends BaseDO<Long> {

    /** 关联的用例 */
    private Long testCaseId;

    /** 关联jmxDO */
    private Long jmxId;

    /** 断言response code */
    private String responseCode;

    /** 断言response text */
    private String responseMessage;

    /** 断言的JSON路径 */
    private String jsonPath;

    /** 预期值 */
    private String expectedValue;
}
