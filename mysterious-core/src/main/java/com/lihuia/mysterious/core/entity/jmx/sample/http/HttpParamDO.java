package com.lihuia.mysterious.core.entity.jmx.sample.http;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
@EqualsAndHashCode(callSuper=true)
public class HttpParamDO extends BaseDO<Long> {

    private static final long serialVersionUID = 31234592228884767L;

    /** 关联的用例 */
    private Long testCaseId;

    /** 关联JmxDO */
    private Long jmxId;

    /** 关联HttpDO */
    private Long httpId;

    /** param 字段 */
    private String paramKey;

    /** param 字段的值 */
    private String paramValue;
}
