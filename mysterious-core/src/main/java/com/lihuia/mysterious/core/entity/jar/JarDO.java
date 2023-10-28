package com.lihuia.mysterious.core.entity.jar;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author maple@lihuia.com
 * @date 2023/4/1 下午3:18
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class JarDO extends BaseDO<Long> {

    /** 上传前JAR名称 */
    private String srcName;

    /** 上传后JAR名称 */
    private String dstName;

    /** JAR包描述 */
    private String description;

    /** Jar包路径 */
    private String jarDir;

    /** 脚本关联的用例 */
    private Long testCaseId;
}
