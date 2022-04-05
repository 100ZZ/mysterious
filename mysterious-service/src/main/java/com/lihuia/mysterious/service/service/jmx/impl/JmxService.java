package com.lihuia.mysterious.service.service.jmx.impl;

import com.lihuia.mysterious.core.vo.jmx.JmxQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.report.ReportVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseVO;
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
    public Boolean uploadJmx(Long testCaseId, MultipartFile jmxFile) {
        return null;
    }

    @Override
    public Boolean updateJmx(JmxVO jmxVO) {
        return null;
    }

    @Override
    public Boolean deleteJmx(Long id) {
        return null;
    }

    @Override
    public PageVO<JmxVO> getJmxList(JmxQuery jmxQuery) {
        return null;
    }

    @Override
    public Boolean runJmx(CommandLine commandLine, TestCaseVO testCaseVO, ReportVO reportVO) {
        return null;
    }

    @Override
    public Boolean debugJmx(CommandLine commandLine, TestCaseVO testCaseVO, ReportVO reportVO) {
        return null;
    }

    @Override
    public Boolean stopJmx(CommandLine commandLine, TestCaseVO testCaseVO) {
        return null;
    }

    @Override
    public JmxVO getByTestCaseId(Long testCaseId) {
        return null;
    }

    @Override
    public Boolean addOnlineJmx(JmxVO jmxVO) {
        return null;
    }

    @Override
    public JmxVO getOnlineJmx(Long id) {
        return null;
    }

    @Override
    public Boolean updateOnlineJmx(JmxVO jmxVO) {
        return null;
    }

    @Override
    public Boolean forceDelete(Long id) {
        return null;
    }
}
