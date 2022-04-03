package com.lihuia.mysterious.service.service.testcase.impl;

import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseElementVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseFullVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseQuery;
import com.lihuia.mysterious.core.vo.testcase.TestCaseVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.service.testcase.ITestCaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:44
 */

@Service
public class TestCaseService implements ITestCaseService {
    @Override
    public Long addTestCase(TestCaseVO testCaseVO, UserVO userVO) {
        return null;
    }

    @Override
    public Boolean deleteTestCase(Long id) {
        return null;
    }

    @Override
    public Boolean batchDeleteTestCase(List<Long> ids) {
        return null;
    }

    @Override
    public Boolean updateTestCase(TestCaseVO testCaseVO, UserVO userVO) {
        return null;
    }

    @Override
    public PageVO<TestCaseVO> getTestCaseList(TestCaseQuery testCaseQuery) {
        return null;
    }

    @Override
    public TestCaseVO getById(Long id) {
        return null;
    }

    @Override
    public TestCaseElementVO getElement(Long id) {
        return null;
    }

    @Override
    public TestCaseFullVO getFull(Long id) {
        return null;
    }

    @Override
    public Boolean debugTestCase(Long id, UserVO userVO) {
        return null;
    }

    @Override
    public Boolean runTestCase(Long id, UserVO userVO) {
        return null;
    }

    @Override
    public Boolean stopTestCase(Long id, UserVO userVO) {
        return null;
    }

    @Override
    public Boolean updateStatus(Long id, Integer status) {
        return null;
    }

    @Override
    public JmxDO getJmx(Long id) {
        return null;
    }

    @Override
    public List<TestCaseDO> getByStatus(Integer status) {
        return null;
    }

    @Override
    public Boolean syncNodeElement(Long testCaseId, Long nodeId) {
        return null;
    }

    @Override
    public Boolean syncAllNodeElement(Long nodeId) {
        return null;
    }
}
