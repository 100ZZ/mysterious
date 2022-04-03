package com.lihuia.mysterious.service.service.jmx;

import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.vo.jmx.JmxQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import org.apache.commons.exec.CommandLine;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:38
 */

public interface IJmxService {

    /**
     * 上传用例的JMX脚本
     * @param testCaseId
     * @param jmxFile
     */
    void uploadJmx(Long testCaseId, MultipartFile jmxFile);

    /**
     * 更新JMX脚本
     * @param jmxVO
     */
    void updateJmx(JmxVO jmxVO);

    /**
     * 删除JMX脚本
     * @param id
     */
    void deleteJmx(Long id);

    /**
     * 分页查询JMX列表
     * @param jmxQuery
     * @return
     */
    PageVO<JmxVO> getJmxList(JmxQuery jmxQuery);

    /**
     * 运行JMX压测脚本
     * @param commandLine
     * @param testCaseDO
     * @param reportDO
     */
    void runJmx(CommandLine commandLine, TestCaseDO testCaseDO, ReportDO reportDO);

    /**
     * 调试JMX压测脚本，只执行调试脚本一次
     * @param commandLine
     * @param testCaseDO
     * @param reportDO
     */
    void debugJmx(CommandLine commandLine, TestCaseDO testCaseDO, ReportDO reportDO);

    /**
     * 停止JMX脚本压测
     * @param commandLine
     * @param testCaseDO
     */
    void stopJmx(CommandLine commandLine, TestCaseDO testCaseDO);

    /**
     * 查询用例关联的所有JMX脚本
     * @param testCaseId
     * @return
     */
    JmxVO getByTestCaseId(Long testCaseId);
}
