package com.lihuia.mysterious.core.mapper.report;

import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/10 7:57 PM
 */

public interface ReportMapper extends BaseMapper<ReportDO> {


    Integer getReportCount(String name, Long creatorId);

    List<ReportDO> getReportList(String name, Long creatorId, Integer offset, Integer limit);

    Integer getReportCountByTestCase(String name, Long testCaseId, Long creatorId);

    List<ReportDO> getReportListByTestCase(String name, Long testCaseId, Long creatorId, Integer offset, Integer limit);

    /** 根据执行类型查询数量 */
    Integer getReportCountByExecType(Integer execType);

    List<ReportDO> getReportByType(Long testCaseId, Integer execType, Integer limit);

    void updateReportStatus(Long id, Integer status);
}
