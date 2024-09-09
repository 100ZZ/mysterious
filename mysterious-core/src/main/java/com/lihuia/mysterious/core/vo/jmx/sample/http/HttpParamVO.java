package com.lihuia.mysterious.core.vo.jmx.sample.http;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
@EqualsAndHashCode(callSuper=true)
public class HttpParamVO extends BaseVO<Long> {

    /** 关联的用例 */
    private Long testCaseId;

    /** 关联JmxDO */
    private Long jmxId;

    /** 关联HttpDO */
    private Long httpId;

    /** 字段 */
    private String paramKey;

    /** 字段的值 */
    private String paramValue;
}
