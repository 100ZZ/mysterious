package com.lihuia.mysterious.core.mapper.jmx.sample.java;


import com.lihuia.mysterious.core.entity.jmx.sample.java.JavaParamDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:33
 */

public interface JavaParamMapper extends BaseMapper<JavaParamDO> {

    List<JavaParamDO> getListByJavaId(Long javaId);

    List<JavaParamDO> getListByJmxId(Long jmxId);

    List<JavaParamDO> getExistParamList(Long javaId, String name);
}
