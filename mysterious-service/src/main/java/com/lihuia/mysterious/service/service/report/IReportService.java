package com.lihuia.mysterious.service.service.report;

import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.report.ReportByTestCaseQuery;
import com.lihuia.mysterious.core.vo.report.ReportQuery;
import com.lihuia.mysterious.core.vo.report.ReportVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.List;

/**
 * @author lihuia.com
 * @date 2023/4/5 5:44 PM
 */

public interface IReportService {

    /**
     * 所有用例的所有报告列表
     * @param query
     * @return
     */
    PageVO<ReportVO> getReportList(ReportQuery query);

    /**
     * 用例页面，点击报告，list出该用例相关的报告
     * @param query
     * @return
     */
    PageVO<ReportVO> getReportListByTestCase(ReportByTestCaseQuery query);


    /**
     * 根据用例id查询测试报告
     * @param testCaseId
     * @param execType
     * @param limit
     * @return
     */
    List<ReportVO> getDebugReportListByTestCaseId(Long testCaseId, Integer execType, Integer limit);

    /**
     * 查询报告详情
     * @param id
     * @return
     */
    ReportVO getById(Long id);

    /**
     * 新增报告
     * @param reportVO
     * @param userVO
     * @return
     */
    Long addReport(ReportVO reportVO, UserVO userVO);

    /**
     * 删除报告
     * @param id
     */
    Boolean deleteReport(Long id);

    /**
     * 清理报告，包括磁盘目录
     * @param id
     */
    Boolean cleanReport(Long id);

    /**
     * 下载报告
     * @param reportVO
     * @return
     * @throws IOException
     */
    FileSystemResource downloadReport(ReportVO reportVO) throws IOException;

    /**
     * 预览压测报告内容
     * @param id
     * @return
     */
    String viewReport(Long id);

    /**
     * 更新历史数据的debug=》压测状态，不对外
     */
    Boolean updateReportExecType();
}
