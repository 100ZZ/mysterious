package com.lihuia.mysterious.core.mapper.testcase;

import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author maple@lihuia.com
 * @date 2023/4/1 下午3:34
 */

public interface TestCaseMapper extends BaseMapper<TestCaseDO> {

    Integer getTestCaseCount(Long id, String name, String biz, String service, Long creatorId);

    List<TestCaseDO> getTestCaseList(Long id, String name, String biz, String service, Long creatorId, Integer offset, Integer limit);

    List<TestCaseDO> getTestCaseListByStatus(Integer status);

    TestCaseDO getTestCaseByName(String name);

    int updateStatus(Long id, Integer status);
}
