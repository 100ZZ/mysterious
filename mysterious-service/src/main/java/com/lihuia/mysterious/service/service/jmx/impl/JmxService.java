package com.lihuia.mysterious.service.service.jmx.impl;

import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.vo.jmx.JmxQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.jmx.IJmxService;
import org.apache.commons.exec.CommandLine;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lihuia.com
 * @date 2022/4/2 11:26 AM
 */

@Service
public class JmxService implements IJmxService {

    @Override
    public void uploadJmx(Long testCaseId, MultipartFile jmxFile) {

    }

    @Override
    public void updateJmx(JmxVO jmxVO) {

    }

    @Override
    public void deleteJmx(Long id) {

    }

    @Override
    public PageVO<JmxVO> getJmxList(JmxQuery jmxQuery) {
        return null;
    }

    @Override
    public void runJmx(CommandLine commandLine, TestCaseDO testCaseDO, ReportDO reportDO) {

    }

    @Override
    public void debugJmx(CommandLine commandLine, TestCaseDO testCaseDO, ReportDO reportDO) {

    }

    @Override
    public void stopJmx(CommandLine commandLine, TestCaseDO testCaseDO) {

    }

    @Override
    public JmxVO getByTestCaseId(Long testCaseId) {
        return null;
    }
}
