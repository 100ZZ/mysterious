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
    Boolean uploadJmx(Long testCaseId, MultipartFile jmxFile);

    /**
     * 更新JMX脚本
     * @param jmxVO
     */
    Boolean updateJmx(JmxVO jmxVO);

    /**
     * 删除JMX脚本
     * @param id
     */
    Boolean deleteJmx(Long id);

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
    Boolean runJmx(CommandLine commandLine, TestCaseDO testCaseDO, ReportDO reportDO);

    /**
     * 调试JMX压测脚本，只执行调试脚本一次
     * @param commandLine
     * @param testCaseDO
     * @param reportDO
     */
    Boolean debugJmx(CommandLine commandLine, TestCaseDO testCaseDO, ReportDO reportDO);

    /**
     * 停止JMX脚本压测
     * @param commandLine
     * @param testCaseDO
     */
    Boolean stopJmx(CommandLine commandLine, TestCaseDO testCaseDO);

    /**
     * 查询用例关联的所有JMX脚本
     * @param testCaseId
     * @return
     */
    JmxVO getByTestCaseId(Long testCaseId);

    /**
     * 在线新增JMX脚本
     * @param jmxVO
     */
    Boolean addOnlineJmx(JmxVO jmxVO);

    /**
     * 在线编辑时，先要获取所有内容
     * @param id
     * @return
     */
    JmxVO getOnlineJmx(Long id);

    /**
     * 编辑更新在线脚本
     * @param jmxVO
     */
    Boolean updateOnlineJmx(JmxVO jmxVO);

    /**
     * 强制删除jmx所有相关的信息，不对外开放，不关注合理性
     * @param id
     */
    Boolean forceDelete(Long id);
}
