package com.lihuia.mysterious.core.entity.jmx.sample.java;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
public class JavaParamDO extends BaseDO<Long> {

    private static final long serialVersionUID = 31234592228884767L;

    /** 关联的用例 */
    private Long testCaseId;

    /** 关联的脚本 */
    private Long jmxId;

    /** 关联的JavaDO */
    private Long javaId;

    /** 名称 */
    private String paramKey;

    /** 值 */
    private String paramValue;
}
