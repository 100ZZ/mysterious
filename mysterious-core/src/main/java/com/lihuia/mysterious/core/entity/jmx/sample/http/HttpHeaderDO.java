package com.lihuia.mysterious.core.entity.jmx.sample.http;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
public class HttpHeaderDO extends BaseDO<Long> {

    private static final long serialVersionUID = 31234592228884767L;

    /** 关联的用例 */
    private Long testCaseId;

    /** 关联jmxDO */
    private Long jmxId;

    /** 关联HttpDO */
    private Long httpId;

    /** header 的key */
    private String headerKey;

    /** header 的值 */
    private String headerValue;
}
