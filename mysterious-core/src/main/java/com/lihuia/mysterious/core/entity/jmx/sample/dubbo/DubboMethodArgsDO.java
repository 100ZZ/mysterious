package com.lihuia.mysterious.core.entity.jmx.sample.dubbo;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/10/21 10:59
 */

@Data
public class DubboMethodArgsDO extends BaseDO<Long>{

    /** 用例ID */
    private Long testCaseId;

    /** 脚本ID */
    private Long jmxId;

    /** Dubbo请求ID */
    private Long dubboId;

    /** 参数类型 */
    private String paramType;

    /** 参数值 */
    private String paramValue;
}
