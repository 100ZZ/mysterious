package com.lihuia.mysterious.core.mapper.jar;

import com.lihuia.mysterious.core.entity.jar.JarDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:33
 */

public interface JarMapper extends BaseMapper<JarDO> {

    Integer getJarCount(String srcName, Long testCaseId);

    List<JarDO> getJarList(String srcName, Long testCaseId, Integer offset, Integer limit);

    List<JarDO> getByTestCaseId(Long testCaseId);

    List<JarDO> getExistJarList(Long testCaseId, String srcName, String jarDir);
}
