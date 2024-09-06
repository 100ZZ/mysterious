package com.lihuia.mysterious.core.entity.jmx.sample.java;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
public class JavaDO extends BaseDO<Long> {

    private static final long serialVersionUID = 31234592228884767L;

    /** 用例 */
    private Long testCaseId;

    /** 脚本 */
    private Long jmxId;

    /** java request依赖jar包路径 */
    private String javaRequestClassPath;

    /** java request的参数 */
    private List<JavaParamDO> javaParamDOList;
}
