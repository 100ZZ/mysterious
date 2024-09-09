package com.lihuia.mysterious.core.vo.jmx.sample.java;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
public class JavaParamVO extends BaseVO<Long> {

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
