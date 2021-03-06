package com.lihuia.mysterious.service.service.testcase.impl;

import com.alibaba.fastjson.JSON;
import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.io.MysteriousFileUtils;
import com.lihuia.mysterious.common.jmeter.JMeterUtil;
import com.lihuia.mysterious.common.ping.PingUtil;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.common.ssh.SSHUtils;
import com.lihuia.mysterious.common.time.MysteriousTimeUtil;
import com.lihuia.mysterious.core.entity.csv.CsvDO;
import com.lihuia.mysterious.core.entity.jar.JarDO;
import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.mapper.testcase.TestCaseMapper;
import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.report.ReportVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseFullVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseParam;
import com.lihuia.mysterious.core.vo.testcase.TestCaseQuery;
import com.lihuia.mysterious.core.vo.testcase.TestCaseVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.enums.ExecTypeEnum;
import com.lihuia.mysterious.service.enums.NodeStatusEnum;
import com.lihuia.mysterious.service.enums.TestCaseStatus;
import com.lihuia.mysterious.service.redis.RedisService;
import com.lihuia.mysterious.service.service.config.IConfigService;
import com.lihuia.mysterious.service.service.csv.ICsvService;
import com.lihuia.mysterious.service.service.jar.IJarService;
import com.lihuia.mysterious.service.service.jmx.IJmxService;
import com.lihuia.mysterious.service.service.node.INodeService;
import com.lihuia.mysterious.service.service.report.IReportService;
import com.lihuia.mysterious.service.service.testcase.ITestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lihuia.com
 * @date 2022/4/1 ??????4:44
 */

@Slf4j
@Service
public class TestCaseService implements ITestCaseService {

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private IConfigService configService;

    @Autowired
    private INodeService nodeService;

    @Autowired
    private IJmxService jmxService;

    @Autowired
    private IJarService jarService;

    @Autowired
    private ICsvService csvService;

    @Autowired
    private IReportService reportService;

    @Autowired
    private MysteriousFileUtils fileUtils;

    @Autowired
    private MysteriousTimeUtil timeUtil;

    @Autowired
    private PingUtil pingUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CRUDEntity<TestCaseDO> crudEntity;

    private void checkTestCaseParam(TestCaseParam testCaseParam) {
        if (ObjectUtils.isEmpty(testCaseParam)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
    }

    @Transactional
    @Override
    public Long addTestCase(TestCaseParam testCaseParam, UserVO userVO) {
        if (StringUtils.isEmpty(testCaseParam.getName())
                || testCaseParam.getName().contains(" ")
                || testCaseParam.getName().contains("#")) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_NAME_ERROR);
        }
        if (testCaseMapper.getTestCaseByName(testCaseParam.getName()) != null) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_IS_EXIST);
        }
        /** master???????????????jmx???jar???csv??????????????? */
        String mysteriousDataHome = configService.getValue(JMeterUtil.MASTER_DATA_HOME);
        String masterTestCasePath = mysteriousDataHome + File.separator
                + testCaseParam.getName() + "_" + timeUtil.getCurrentTime() + File.separator;
        /** ?????????????????????????????????????????????????????????????????????????????????createTime???????????????????????? */
        TestCaseDO testCaseDO = BeanConverter.doSingle(testCaseParam, TestCaseDO.class);
        /** ???????????? */
        testCaseDO.setTestCaseDir(masterTestCasePath);
        /** ?????????????????????????????? */
        testCaseDO.setStatus(TestCaseStatus.NOT_RUN.getCode());
        crudEntity.addT(testCaseDO, userVO);
        /** ???????????? */
        log.info("????????????: {}", JSON.toJSONString(testCaseDO, true));
        testCaseMapper.add(testCaseDO);
        /** ???????????? */
        fileUtils.mkDir(masterTestCasePath);
        return testCaseDO.getId();
    }

    @Override
    public Boolean deleteTestCase(Long id) {
        if (ObjectUtils.isEmpty(testCaseMapper.getById(id))) {
            return false;
        }
        return testCaseMapper.delete(id) > 0;
    }

    @Override
    public Boolean batchDeleteTestCase(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            ids.forEach(this::deleteTestCase);
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean updateTestCase(Long id, TestCaseParam testCaseParam, UserVO userVO) {
        checkTestCaseParam(testCaseParam);

        TestCaseDO dbTestCaseDO = testCaseMapper.getById(id);
        if (ObjectUtils.isEmpty(dbTestCaseDO)) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_NOT_EXIST);
        }
        if (StringUtils.isEmpty(testCaseParam.getName())
                || testCaseParam.getName().contains(" ")
                || testCaseParam.getName().contains("#")) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_NAME_ERROR);
        }
        /** ?????????????????????, ????????????jmx???jar???csv?????? */
        if (!dbTestCaseDO.getName().equals(testCaseParam.getName())) {
            log.info("????????????");
            JmxDO jmxDO = dbTestCaseDO.getJmxDO();
            if (jmxDO != null) {
                log.info("????????????????????????JMX????????????: {}", testCaseParam.getName());
                JmxVO jmxVO = BeanConverter.doSingle(jmxDO, JmxVO.class);
                jmxVO.setId(jmxDO.getId());
                jmxVO.setDescription(testCaseParam.getName());
                jmxService.updateJmx(jmxVO, userVO);
            }
            List<JarDO> jarDOList = dbTestCaseDO.getJarDOList();
            if (!CollectionUtils.isEmpty(jarDOList)) {
                jarDOList.forEach(jarDO -> {
                    log.info("????????????????????????JAR???????????????: {}", testCaseParam.getName());
                    JarVO jarVO = BeanConverter.doSingle(jarDO, JarVO.class);
                    jarVO.setId(jarDO.getId());
                    jarVO.setDescription(testCaseParam.getName());
                    jarService.updateJar(jarVO, userVO);
                });
            }
            List<CsvDO> csvDOList = dbTestCaseDO.getCsvDOList();
            if (!CollectionUtils.isEmpty(csvDOList)) {
                csvDOList.forEach(csvDO ->  {
                    log.info("????????????????????????CSV????????????: {}", testCaseParam.getName());
                    CsvVO csvVO = BeanConverter.doSingle(csvDO, CsvVO.class);
                    csvVO.setId(csvDO.getId());
                    csvVO.setDescription(testCaseParam.getName());
                    csvService.updateCsv(csvVO, userVO);
                });
            }
        }

        TestCaseDO testCaseDO = BeanConverter.doSingle(testCaseParam, TestCaseDO.class);
        crudEntity.updateT(testCaseDO, userVO);
        log.info("????????????: {}", JSON.toJSONString(testCaseDO));
        return testCaseMapper.update(testCaseDO) > 0;
    }

    @Override
    public PageVO<TestCaseVO> getTestCaseList(TestCaseQuery query) {
        PageVO<TestCaseVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(query.getPage(), query.getSize());
        Integer total = testCaseMapper.getTestCaseCount(
                        query.getId(), query.getName(), query.getBiz(), query.getService(), query.getCreatorId());
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<TestCaseDO> testCaseDOList =
                    testCaseMapper.getTestCaseList(
                            query.getId(), query.getName(), query.getBiz(), query.getService(), query.getCreatorId(), offset, query.getSize());
            if (!CollectionUtils.isEmpty(testCaseDOList)) {
                pageVO.setList(testCaseDOList.stream().map(testCaseDO -> {
                    TestCaseVO testCaseVO = BeanConverter.doSingle(testCaseDO, TestCaseVO.class);
                    testCaseVO.setId(testCaseDO.getId());
                    return testCaseVO;
                }).collect(Collectors.toList()));
            }
        }
        return pageVO;
    }

    @Override
    public TestCaseVO getById(Long id) {
        TestCaseDO testCaseDO = testCaseMapper.getById(id);
        if (!ObjectUtils.isEmpty(testCaseDO)) {
            TestCaseVO testCaseVO = BeanConverter.doSingle(testCaseDO, TestCaseVO.class);
            testCaseVO.setId(id);
            return testCaseVO;
        }
        return null;
    }

    @Override
    public TestCaseFullVO getFullVO(Long id) {
        TestCaseDO testCaseDO = testCaseMapper.getById(id);
        if (!ObjectUtils.isEmpty(testCaseDO)) {
            TestCaseFullVO testCaseFullVO = BeanConverter.doSingle(testCaseDO, TestCaseFullVO.class);
            testCaseFullVO.setJmxVO(jmxService.getByTestCaseId(id));
            testCaseFullVO.setCsvVOList(csvService.getByTestCaseId(id));
            testCaseFullVO.setJarVOList(jarService.getByTestCaseId(id));
            return testCaseFullVO;
        }
        return null;
    }

    private TestCaseDO getFullDO(Long id) {
        TestCaseDO testCaseDO = testCaseMapper.getById(id);
        if (!ObjectUtils.isEmpty(testCaseDO)) {
            testCaseDO.setJmxDO(jmxService.getJmxDO(id));
            testCaseDO.setCsvDOList(csvService.getCsvDOList(id));
            testCaseDO.setJarDOList(jarService.getJarDOList(id));
            return testCaseDO;
        }
        return null;
    }

    @Transactional
    @Override
    public Boolean debugTestCase(Long id, UserVO userVO) {
            /** ????????????????????????Master????????????jmeter?????????????????? */
            TestCaseDO testCaseDO = getFullDO(id);

            log.info("testCaseDO: {}", testCaseDO);
            /** ??????????????????????????? */
            JmxDO jmxDO = testCaseDO.getJmxDO();
            if (ObjectUtils.isEmpty(jmxDO)) {
                throw new MysteriousException(ResponseCodeEnum.JMX_NOT_EXIST);
            }

            if (!ObjectUtils.isEmpty(userVO)) {
                crudEntity.updateT(testCaseDO, userVO);
                testCaseMapper.update(testCaseDO);
            }

            /** ????????????????????????????????? debug_xxx.jmx */
            String jmxFilePath = jmxDO.getJmxDir() + "debug_" + jmxDO.getDstName();
            log.info("????????????: {}", jmxFilePath);

            String currentTime = timeUtil.getCurrentTime();
            /**  ??????????????????????????????, ???????????? */
            String reportDir = testCaseDO.getTestCaseDir() + "report" + File.separator + currentTime + File.separator;
            fileUtils.mkDir(reportDir);

            /** jtl???????????? */
            String jtlDir = reportDir + "jtl" + File.separator;
            fileUtils.mkDir(jtlDir);
            /** jtl?????????????????? */
            String jtlFile = testCaseDO.getName() + ".xml";
            String jtlFilePath = jtlDir + jtlFile;

            /** ??????????????????????????? */
            String reportFilePath = reportDir + "data" + File.separator;
            fileUtils.mkDir(reportFilePath);

            /** jmeter.log??????????????? */
            String jmeterLogDir = reportDir + "log" + File.separator;
            fileUtils.mkDir(jmeterLogDir);
            /** log?????? */
            String jmeterLogFile = "jmeter_" + currentTime + ".log";
            String jmeterLogFilePath = jmeterLogDir + jmeterLogFile;

            ReportVO reportVO = new ReportVO();
            reportVO.setTestCaseId(testCaseDO.getId());
            reportVO.setJmeterLogFilePath(jmeterLogFilePath);
            reportVO.setName(testCaseDO.getName());
            reportVO.setExecType(ExecTypeEnum.DEBUG.getType());
            reportVO.setDescription("???" + currentTime + "???" + testCaseDO.getDescription());
            /** ????????????????????????????????????????????????responseData ??? xml??????????????? */
            reportVO.setReportDir(jtlDir);

            /** ??????????????????JMX???????????? */
            /** ??????????????????????????? */

            /** jmeter????????????????????? */
            String masterJmeterBinHome = configService.getValue(JMeterUtil.MASTER_JMETER_BIN_HOME);
            String masterJmeterApplication = masterJmeterBinHome + File.separator + "jmeter";

            /** ??????????????? */
            CommandLine commandLine = new CommandLine(masterJmeterApplication);
            commandLine.addArgument("-Jjmeter.save.saveservice.output_format=xml");
            commandLine.addArgument("-Jjmeter.save.saveservice.response_data=true");
            commandLine.addArgument("-n");
            commandLine.addArgument("-t");
            commandLine.addArgument(jmxFilePath);
            commandLine.addArgument("-l");
            commandLine.addArgument(jtlFilePath);
            commandLine.addArgument("-j");
            commandLine.addArgument(jmeterLogFilePath);
//        commandLine.addArgument("-e");
//        commandLine.addArgument("-o");
//        commandLine.addArgument(reportFilePath);

            /** ??????master???????????? */

            /** ????????? */
            testCaseDO.setStatus(TestCaseStatus.RUN_ING.getCode());
            testCaseMapper.update(testCaseDO);

            /** ????????????????????????????????????????????????????????????????????????????????? */
            log.info("??????????????????: {}", JSON.toJSONString(reportVO, true));
            Long reportId = reportService.addReport(reportVO, userVO);

            log.info("[debugTestCase]commandLine: {}", commandLine.toString()
                    .replace(",", "").replace("[", "").replace("]", ""));

            jmxService.debugJmx(commandLine, id, reportId, userVO);
        return null;
    }

    @Override
    public Boolean runTestCase(Long id, UserVO userVO) {
        List<ReportVO> reportVOList = reportService.getDebugReportListByTestCaseId(id, ExecTypeEnum.DEBUG.getType(), 1);
        if (CollectionUtils.isEmpty(reportVOList) || !reportVOList.get(0).getStatus().equals(TestCaseStatus.RUN_SUCCESS.getCode())) {
            throw new MysteriousException(ResponseCodeEnum.DEBUG_BEFORE_RUN);
        }
        List<NodeVO> enableNodeList = nodeService.getEnableNodeList();
        /** slave??????????????????enable??????????????????????????? */
        for (NodeVO nodeVO : enableNodeList) {
            try {
                pingUtil.ping(nodeVO.getHost());
            } catch (IOException e) {
                throw new MysteriousException(ResponseCodeEnum.NODE_CANNOT_CONNECT);
            }
        }

        TestCaseDO testCaseDO = getFullDO(id);

        if (TestCaseStatus.RUN_ING.getCode().equals(testCaseDO.getStatus())) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_IS_RUNNING);
        }
        /** ??????????????????????????? */
        JmxVO jmxVO = jmxService.getByTestCaseId(id);
        if (ObjectUtils.isEmpty(jmxVO)) {
            throw new MysteriousException(ResponseCodeEnum.JMX_NOT_EXIST);
        }

        crudEntity.updateT(testCaseDO, userVO);
        if (!ObjectUtils.isEmpty(userVO)) {
            testCaseMapper.update(testCaseDO);
        }

        /** ????????????????????? */
        /** ??????????????????????????????????????????id */
        List<TestCaseDO> runningTestCaseList =
                testCaseMapper.getTestCaseListByStatus(TestCaseStatus.RUN_ING.getCode());
        if (!CollectionUtils.isEmpty(runningTestCaseList)) {
            log.info("??????????????????????????????,????????????,??????id={}", id);
            /**  ????????????key */
            if (redisService.listContainsCaseId(id)) {
                log.warn("?????????????????????????????? testCaseId: {}", id);
                return true;
            }
            /** ????????????????????????????????????????????????????????? */
            testCaseDO.setStatus(TestCaseStatus.RUN_WAITING.getCode());
            testCaseMapper.update(testCaseDO);
            redisService.pushCaseIdToList(id);
            return true;
        }

        String jmxFilePath = jmxVO.getJmxDir() + jmxVO.getDstName();

        String currentTime = timeUtil.getCurrentTime();
        /**  ??????????????????????????????, ???????????? */
        String reportDir = testCaseDO.getTestCaseDir() + "report" + File.separator + currentTime + File.separator;
        fileUtils.mkDir(reportDir);

        /** jtl???????????? */
        String jtlDir = reportDir + "jtl"+ File.separator ;
        fileUtils.mkDir(jtlDir);
        /** jtl?????????????????? */
        String jtlFile = testCaseDO.getName() + ".jtl";
        String jtlFilePath = jtlDir + jtlFile;

        /** ??????????????????????????? */
        String reportFilePath = reportDir + "data" + File.separator;
        fileUtils.mkDir(reportFilePath);

        /** jmeter.log??????????????? */
        String jmeterLogDir = reportDir + "log" + File.separator;
        fileUtils.mkDir(jmeterLogDir);
        /** log?????? */
        String jmeterLogFile = "jmeter_" + currentTime + ".log";
        String jmeterLogFilePath = jmeterLogDir + jmeterLogFile;

        ReportVO reportVO = new ReportVO();
        reportVO.setTestCaseId(testCaseDO.getId());
        /** jtl??????????????????????????????????????????jmeter.log????????? */
        reportVO.setJmeterLogFilePath(jmeterLogFilePath);
        reportVO.setName(testCaseDO.getName());
        reportVO.setExecType(ExecTypeEnum.EXEC.getType());
        reportVO.setDescription("???" + currentTime + "???" + testCaseDO.getDescription());
        reportVO.setReportDir(reportFilePath);

        /** ??????????????????JMX???????????? */
        /** ??????????????????????????? */

        /** jmeter????????????????????? */
        String masterJmeterBinHome = configService.getValue(JMeterUtil.MASTER_JMETER_BIN_HOME);
        String masterJmeterApplication = masterJmeterBinHome + File.separator + "jmeter";

        /** ??????????????? */
        CommandLine commandLine = new CommandLine(masterJmeterApplication);
        commandLine.addArgument("-n");
        commandLine.addArgument("-t");
        commandLine.addArgument(jmxFilePath);
        /** ????????????????????????slave??????, ???????????????-R????????????????????? */
        //List<NodeDO> enableNodeList = nodeService.getEnableNodeList();
        if (!CollectionUtils.isEmpty(enableNodeList)) {
            commandLine.addArgument("-R");
            String slaveNodes = enableNodeList.stream().map(NodeVO::getHost).collect(Collectors.joining(","));
            commandLine.addArgument(slaveNodes);
        }
        commandLine.addArgument("-l");
        commandLine.addArgument(jtlFilePath);
        commandLine.addArgument("-j");
        commandLine.addArgument(jmeterLogFilePath);
        commandLine.addArgument("-e");
        commandLine.addArgument("-o");
        commandLine.addArgument(reportFilePath);

        /** ?????????JMX???????????????????????????jmeter.properties?????????JAR??????????????????????????? */
        /** csv????????????????????????jmx????????????????????????????????????????????????jar???????????????????????????????????????????????????
         * ????????????A???????????????jar??????????????????jar??????????????????B???????????????????????????????????????????????????????????????
         * ???????????????????????????????????????jar????????????????????????
         * */

        /** ??????master???slave?????????????????????jar????????? */
        /** ??????????????????jar??????????????????jmx?????????classpath */
        //jarService.updateJarDependency(testCaseDO.getTestCaseDir());

        /** ????????? */
        /** ????????????????????????????????????startCaseFromRedis????????????????????????????????????SSO?????????????????????????????????SSO??????????????? */
        testCaseDO.setStatus(TestCaseStatus.RUN_ING.getCode());
        testCaseMapper.update(testCaseDO);

        /** ?????? */
        Long reportId = reportService.addReport(reportVO, userVO);
        log.info("??????????????????: {}", JSON.toJSONString(reportVO, true));

        log.info("[runTestCase]commandLine: {}", commandLine.toString()
                .replace(",", "").replace("[", "").replace("]", ""));

        jmxService.runJmx(commandLine, id, reportId, userVO);
        return true;
    }

    @Transactional
    @Override
    public Boolean stopTestCase(Long id, UserVO userVO) {
        TestCaseDO testCaseDO = testCaseMapper.getById(id);

        if (!TestCaseStatus.RUN_ING.getCode().equals(testCaseDO.getStatus())) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_IS_NOT_RUNNING);
        }

        JmxVO jmxVO = jmxService.getByTestCaseId(id);
        if (ObjectUtils.isEmpty(jmxVO)) {
            throw new MysteriousException(ResponseCodeEnum.JMX_NOT_EXIST);
        }
        /** jmeter?????????????????????, shutdown.sh */
        String masterJmeterBinHome = configService.getValue(JMeterUtil.MASTER_JMETER_BIN_HOME);
        String jmxStopScriptPath = masterJmeterBinHome + File.separator + "shutdown.sh";

        CommandLine commandLine = new CommandLine(jmxStopScriptPath);
        /** ?????????master??????????????????????????????????????????????????????????????????master????????????????????????master????????????shutdown.sh */
        log.info("[stopTestCase]commandLine: {}", commandLine.toString()
                .replace(",", "").replace("[", "").replace("]", ""));

        jmxService.stopJmx(commandLine, id, userVO);
        return true;
    }

    @Override
    public Boolean updateStatus(Long id, Integer status) {
        if (ObjectUtils.isEmpty(testCaseMapper.getById(id))) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_NOT_EXIST);
        }
        return testCaseMapper.updateStatus(id, status) > 0;
    }

    @Override
    public List<TestCaseDO> getByStatus(Integer status) {
        return testCaseMapper.getTestCaseListByStatus(status);
    }

    @Override
    public Boolean syncNode(Long nodeId) {
        NodeVO nodeVO = nodeService.getById(nodeId);
        if (ObjectUtils.isEmpty(nodeVO)) {
            throw new MysteriousException(ResponseCodeEnum.NODE_NOT_EXIST);
        }
        if (NodeStatusEnum.ENABLE.getCode().equals(nodeVO.getStatus())) {
            throw new MysteriousException(ResponseCodeEnum.NODE_IS_ENABLE);
        }
        List<TestCaseDO> testCaseList = testCaseMapper.getTestCaseListByStatus(null);
        if (!CollectionUtils.isEmpty(testCaseList)) {
            testCaseList.forEach(testCaseDO -> this.syncNodeElement(testCaseDO.getId(), nodeId));
        }
        return true;
    }

    private void syncNodeElement(Long testCaseId, Long nodeId) {
        NodeVO nodeVO = nodeService.getById(nodeId);
        SSHUtils ssh = new SSHUtils(nodeVO.getHost(), nodeVO.getPort(), nodeVO.getUsername(), nodeVO.getPassword());

        TestCaseFullVO testCaseFullVO = getFullVO(testCaseId);
        /** ???????????????slave??????????????????????????????????????????????????????????????? */
        /** jmx??????????????????master????????????????????????????????? */
        List<CsvVO> csvVOList = testCaseFullVO.getCsvVOList();
        if (!CollectionUtils.isEmpty(csvVOList)) {
            for (CsvVO csvVO : csvVOList) {
                String csvDir = csvVO.getCsvDir();
                String csvFilePath = csvDir + csvVO.getSrcName();
                ssh.scpFile(csvFilePath, csvDir);
            }
            log.info("csv??????????????????");
        }

        List<JarVO> jarVOList = testCaseFullVO.getJarVOList();
        if (!CollectionUtils.isEmpty(jarVOList)) {
            for (JarVO jarVO : jarVOList) {
                String jarDir = jarVO.getJarDir();
                String jarFilePath = jarDir + jarVO.getSrcName();
                ssh.scpFile(jarFilePath, jarDir);
            }
            log.info("jar??????????????????");
        }
    }
}
