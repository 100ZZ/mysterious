package com.lihuia.mysterious.service.handler.result;

import com.alibaba.fastjson.JSON;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.mapper.report.ReportMapper;
import com.lihuia.mysterious.service.enums.TestCaseStatus;
import com.lihuia.mysterious.service.handler.dto.ResultDTO;
import com.lihuia.mysterious.service.redis.TestCaseRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.ExecuteException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lihuia.com
 * @date 2022/4/14 10:36 PM
 */

@Slf4j
public class DebugResultHandler extends ResultHandler {

    private ReportDO reportDO;

    private ReportMapper reportMapper;

    private TestCaseRedisService testCaseRedisService;

    private Boolean isDebugOver;

    public DebugResultHandler(ResultDTO resultDTO) {
        super(resultDTO);
        this.testCaseDO = resultDTO.getTestCaseDO();
        this.reportDO = resultDTO.getReportDO();
        this.testCaseMapper = resultDTO.getTestCaseMapper();
        this.reportMapper = resultDTO.getReportMapper();
        this.outputStream = resultDTO.getOutputStream();
        this.testCaseRedisService = resultDTO.getTestCaseRedisService();
    }

    /**
     * jmx脚本调试成功会走到这里
     * 重写父类方法，增加入库及日志打印
     */
    @Override
    public void onProcessComplete(final int exitValue) {
        log.info("用例:{}, 调试成功", testCaseDO.getName());
        testCaseDO.setStatus(TestCaseStatus.RUN_SUCCESS.getCode());
        reportDO.setStatus(TestCaseStatus.RUN_SUCCESS.getCode());
        log.info("用例:{}, 更新用例执行状态: {}", testCaseDO.getName(), testCaseDO.getStatus());
        testCaseMapper.update(testCaseDO);
        reportMapper.updateReportStatus(reportDO.getId(), TestCaseStatus.RUN_SUCCESS.getCode());
        super.onProcessComplete(exitValue);
        //保存状态，执行完毕
        isDebugOver = true;
        checkJMeterLog(reportDO);
        testCaseRedisService.startCaseFromRedis();
    }

    /**
     * jmx脚本调试失败会走到这里
     * 重写父类方法，增加入库及日志打印
     */
    @Override
    public void onProcessFailed(final ExecuteException e) {
        log.info("用例:{}, 调试失败", testCaseDO.getName());
        /** 用例已经调试成功了，如果后面有失败的，也不更新状态 */
        if (testCaseDO != null && !isDebugOver) {
            testCaseDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            log.info("用例:{}, 更新用例执行状态: {}", testCaseDO.getName(), testCaseDO.getStatus());
            testCaseMapper.update(testCaseDO);
            reportDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            reportMapper.updateReportStatus(reportDO.getId(), TestCaseStatus.RUN_FAILED.getCode());
        }
        super.onProcessFailed(e);
        checkJMeterLog(reportDO);
        testCaseRedisService.startCaseFromRedis();
    }

    private void checkJMeterLog(ReportDO reportDO) {
        log.info("jmeter.log文件:{}", reportDO.getJmeterLogFilePath());

        String[] s = reportDO.getJmeterLogFilePath().split("jmeter");
        String jmeterLogPath = s[0];
        String jmeterLogFile = "jmeter" + s[1];
        log.info("用例:{}, jmeter.log文件目录,{};文件,{}", testCaseDO.getName(), jmeterLogPath, jmeterLogFile);
        Long fileSize = getFileSize(jmeterLogPath + jmeterLogFile);
        /** 如果调试，日志过大，不再进行上传，有可能是解析失败，变成了压测 */
        if (null != fileSize && fileSize >= 1024 * 1024) {
            reportDO.setResponseData("调试日志过大, 请确认");
        } else {
            reportDO.setResponseData(getResponseData());
        }
        log.info(JSON.toJSONString(reportDO, true));
        reportMapper.update(reportDO);
        /** jmeter.log是否有beanshell依赖缺失报错,以及jar包缺失报错 */
        String[] regexs = {
                ".*Error invoking bsh method",
                ".*NoClassDefFoundError"
        };
        String final_regex = String.join("|", regexs);
        if (isMatched(jmeterLogPath + jmeterLogFile, final_regex)) {
            testCaseDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            log.info("用例:{}, beanshell缺少依赖jar, 更新用例执行状态: {}", testCaseDO.getName(), testCaseDO.getStatus());
            testCaseMapper.update(testCaseDO);
        }
    }

    private Long getFileSize(String filePath) {
        FileChannel fc = null;
        try {
            File f = new File(filePath);
            if (f.exists() && f.isFile()){
                FileInputStream fis = new FileInputStream(f);
                fc = fis.getChannel();
                return fc.size();
            }else{
                log.info("file doesn't exist or is not a file");
            }
        } catch (IOException e) {
            log.info(String.valueOf(e));
        } finally {
            if (null != fc){
                try {
                    fc.close();
                } catch (IOException e){
                    log.info(String.valueOf(e));
                }
            }
        }
        return null;
    }

    public String getResponseData() {
        String xmlPath = reportDO.getReportDir();
        String xmlFilePath = xmlPath + testCaseDO.getName() + ".xml";

        if (!new File(xmlFilePath).exists()) {
            return null;
        }

        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(new File(xmlFilePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (null == document) {
            throw new MysteriousException(ResponseCodeEnum.XML_ERROR);
        }

        StringBuilder sb = new StringBuilder();
        Element root = document.getRootElement();
        for (Element element : root.elements()) {
            if ("httpSample".equals(element.getName()) || "sample".equals(element.getName())) {
                for (Element responseData : element.elements()) {
                    if ("responseData".equals(responseData.getName()) &&
                            "java.lang.String".equals(responseData.attributeValue("class"))) {
                        sb.append(responseData.getText());
                    }
                }
            }
        }
        log.info("调试结果: {}", sb.toString());
        if (sb.toString().length() > 500) {
            log.info("调试结果太长, 进行截断");
            return sb.toString().substring(0 ,500);
        }
        return sb.toString();
    }

    public boolean isMatched(String filePath, String regex) {
        BufferedReader br = null;
        String line = null;

        try {
            // 根据文件路径创建缓冲输入流
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

            // 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
            while ((line = br.readLine()) != null) {
                Matcher matcher = Pattern.compile(regex).matcher(line);
                if (matcher.find()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
    }
}
