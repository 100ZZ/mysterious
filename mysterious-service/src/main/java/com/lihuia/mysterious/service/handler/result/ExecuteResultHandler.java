package com.lihuia.mysterious.service.handler.result;

import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.mapper.report.ReportMapper;
import com.lihuia.mysterious.service.enums.TestCaseStatus;
import com.lihuia.mysterious.service.handler.dto.ResultDTO;
import com.lihuia.mysterious.service.redis.TestCaseRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.ExecuteException;

/**
 * @author lihuia.com
 * @date 2022/4/14 10:48 PM
 */

@Slf4j
public class ExecuteResultHandler extends ResultHandler {

    private ReportDO reportDO;

    private ReportMapper reportMapper;

    private TestCaseRedisService testCaseRedisService;

    private Boolean isRunOver;

    public ExecuteResultHandler(ResultDTO resultDTO) {
        super(resultDTO);
        this.testCaseDO = resultDTO.getTestCaseDO();
        this.reportDO = resultDTO.getReportDO();
        this.testCaseMapper = resultDTO.getTestCaseMapper();
        this.reportMapper = resultDTO.getReportMapper();
        this.outputStream = resultDTO.getOutputStream();
        this.testCaseRedisService = resultDTO.getTestCaseRedisService();
    }

    /**
     * 用例执行成功会走到这里
     * 重写父类方法，增加入库及日志打印
     */
    @Override
    public void onProcessComplete(final int exitValue) {
        log.info("用例:{}, 压测执行成功", testCaseDO.getName());
        testCaseDO.setStatus(TestCaseStatus.RUN_SUCCESS.getCode());
        log.info("用例:{}, 更新用例执行状态: {}", testCaseDO.getName(), testCaseDO.getStatus());
        //testCaseMapper.update(testCaseDO);
        //存在压测过程中，用户修改用例信息最后又恢复的问题，所以改成直接只更新状态
        testCaseMapper.updateStatus(testCaseDO.getId(), TestCaseStatus.RUN_SUCCESS.getCode());

        reportDO.setStatus(TestCaseStatus.RUN_SUCCESS.getCode());
        reportMapper.updateReportStatus(reportDO.getId(), TestCaseStatus.RUN_SUCCESS.getCode());
        super.onProcessComplete(exitValue);
        /** 命令执行结束，后面如果有上传日志失败等，都不影响用例执行状态 */
        isRunOver = true;
        testCaseRedisService.startCaseFromRedis();

        //保存状态，执行完毕
    }

    /**
     * jmx脚本执行失败会走到这里
     * 重写父类方法，增加入库及日志打印
     */
    @Override
    public void onProcessFailed(final ExecuteException e) {
        log.info("用例:{}, 压测执行失败", testCaseDO.getName());
        /** 用例已经调试成功了，如果后面有失败的，也不更新状态 */
        if (testCaseDO != null && !isRunOver) {
            testCaseDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            log.info("用例:{}, 更新执行状态: {}", testCaseDO.getName(), testCaseDO.getStatus());
            //testCaseMapper.update(testCaseDO);
            testCaseMapper.updateStatus(testCaseDO.getId(), TestCaseStatus.RUN_FAILED.getCode());
            reportDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            reportMapper.updateReportStatus(reportDO.getId(), TestCaseStatus.RUN_FAILED.getCode());
        }
        super.onProcessFailed(e);
        testCaseRedisService.startCaseFromRedis();
    }
}
