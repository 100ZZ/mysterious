package com.lihuia.mysterious.service.handler.result;

import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.mapper.testcase.TestCaseMapper;
import com.lihuia.mysterious.service.enums.TestCaseStatus;
import com.lihuia.mysterious.service.handler.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author maple@lihuia.com
 * @date 2023/4/14 10:09 PM
 */

@Slf4j
public class ResultHandler extends DefaultExecuteResultHandler {

    // 命令正常输出
    ByteArrayOutputStream outputStream;
    // 命令错误输出
    ByteArrayOutputStream errorStream;

    TestCaseDO testCaseDO;

    ReportDO reportDO;

    TestCaseMapper testCaseMapper;

    public ResultHandler(ResultDTO resultDTO) {
        super();
        this.outputStream = resultDTO.getOutputStream();
        this.errorStream = resultDTO.getErrorStream();
        this.testCaseDO = resultDTO.getTestCaseDO();
        this.reportDO = resultDTO.getReportDO();
        this.testCaseMapper = resultDTO.getTestCaseMapper();
    }

    /**
     * 因为JMeter执行脚本，就算脚本执行有误，也会正常执行完，智能通过标准输出，以及错误率来判断是否执行有误
     * @param outputString
     */
    public void checkResult(String outputString) {
        /** 在标准输出检查是否执行失败 */
        String summaryError = "summary\\s+=\\s+0\\s+in.*";
        String resultError = ".*Err:\\s+([1-9][0-9]*)\\s+\\(.*%\\)";
        String runError = ".*Error.*Exception";
        Matcher summaryErrorMatcher = Pattern.compile(summaryError).matcher(outputString);
        Matcher resultErrorMatcher = Pattern.compile(resultError).matcher(outputString);
        Matcher runErrorMatcher = Pattern.compile(runError).matcher(outputString);
        if (summaryErrorMatcher.find()) {
            log.info("JMeter执行脚本，终端标准输出匹配的统计结果, summary: 0");
            testCaseDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            log.info("执行结果异常, 更新用例执行状态: {}", testCaseDO.getStatus());
            testCaseMapper.update(testCaseDO);
            if (null != reportDO) {
                reportDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            }
        } else if (resultErrorMatcher.find()) {
            log.info("JMeter执行脚本，终端标准输出匹配的错误结果, Err: {}", resultErrorMatcher.group(1));
            testCaseDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            log.info("执行结果异常, 更新用例执行状态: {}", testCaseDO.getStatus());
            testCaseMapper.update(testCaseDO);
            if (null != reportDO) {
                reportDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            }
        } else if (runErrorMatcher.find()) {
            log.info("JMeter执行脚本报错，终端标准输出结果: {}", outputString);
            testCaseDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            log.info("执行脚本异常, 更新用例执行状态: {}", testCaseDO.getStatus());
            testCaseMapper.update(testCaseDO);
            if (null != reportDO) {
                reportDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            }
        } else {
            log.info("JMeter执行脚本, 终端标准输出执行结果正常");
        }
    }

    /**
     * jmx脚本执行成功会走到这里
     * 重写父类方法，增加入库及日志打印
     */
    @Override
    public void onProcessComplete(final int exitValue) {
        super.onProcessComplete(exitValue);
        try {
            String outputString = outputStream.toString("UTF-8");
            //String errorString = errorStream.toString("UTF-8");
            log.info("用例:{}, 用例正常执行: {}", testCaseDO.getName(), outputString);
            //log.info("errorStream: {}", errorString);
            checkResult(outputString);
        } catch (UnsupportedEncodingException e) {
            log.info("打印执行结果内容失败", e);
        }
    }

    /**
     * jmx脚本执行失败会走到这里
     * 重写父类方法，增加入库及日志打印
     */
    @Override
    public void onProcessFailed(final ExecuteException e) {
        super.onProcessFailed(e);
        log.info("用例:{}, 启动Jmeter执行脚本失败", testCaseDO.getName(), e);
        try {
            log.info(errorStream.toString("UTF-8"));
        } catch (UnsupportedEncodingException ee) {
            log.info("打印执行结果内容失败", ee);
        }
    }
}
