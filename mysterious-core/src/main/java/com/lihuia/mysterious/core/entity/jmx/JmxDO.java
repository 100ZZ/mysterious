package com.lihuia.mysterious.core.entity.jmx;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午3:20
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class JmxDO extends BaseDO<Long> {

    /** 上传前脚本名称 */
    private String srcName;

    /** 上传后脚本名称 */
    private String dstName;

    /** 脚本描述 */
    private String description;

    /** 脚本路径 */
    private String testCaseJmxDir;

    /** 脚本关联的用例 */
    private Long testCaseId;
}
