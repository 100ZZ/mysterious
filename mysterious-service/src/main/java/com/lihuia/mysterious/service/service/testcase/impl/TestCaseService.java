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
 * @date 2022/4/1 下午4:44
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
        /** master节点用例，jmx，jar，csv，报告目录 */
        String mysteriousDataHome = configService.getValue(JMeterUtil.MASTER_DATA_HOME);
        String masterTestCasePath = mysteriousDataHome + File.separator
                + testCaseParam.getName() + "_" + timeUtil.getCurrentTime() + File.separator;
        /** 用例名目录，带当前时间，免得用户修改了用例名，可以根据createTime很容易定位到目录 */
        TestCaseDO testCaseDO = BeanConverter.doSingle(testCaseParam, TestCaseDO.class);
        /** 目录保存 */
        testCaseDO.setTestCaseDir(masterTestCasePath);
        /** 新建用例，未执行状态 */
        testCaseDO.setStatus(TestCaseStatus.NOT_RUN.getCode());
        crudEntity.addT(testCaseDO, userVO);
        /** 用例入库 */
        log.info("新增用例: {}", JSON.toJSONString(testCaseDO, true));
        testCaseMapper.add(testCaseDO);
        /** 磁盘操作 */
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
        /** 如果用例名有变, 同步更新jmx，jar，csv描述 */
        if (!dbTestCaseDO.getName().equals(testCaseParam.getName())) {
            log.info("同步修改");
            JmxDO jmxDO = dbTestCaseDO.getJmxDO();
            if (jmxDO != null) {
                log.info("更新用例同步更新JMX脚本描述: {}", testCaseParam.getName());
                JmxVO jmxVO = BeanConverter.doSingle(jmxDO, JmxVO.class);
                jmxVO.setId(jmxDO.getId());
                jmxVO.setDescription(testCaseParam.getName());
                jmxService.updateJmx(jmxVO, userVO);
            }
            List<JarDO> jarDOList = dbTestCaseDO.getJarList();
            if (!CollectionUtils.isEmpty(jarDOList)) {
                jarDOList.forEach(jarDO -> {
                    log.info("更新用例同步更新JAR依赖包描述: {}", testCaseParam.getName());
                    JarVO jarVO = BeanConverter.doSingle(jarDO, JarVO.class);
                    jarVO.setId(jarDO.getId());
                    jarVO.setDescription(testCaseParam.getName());
                    jarService.updateJar(jarVO, userVO);
                });
            }
            List<CsvDO> csvDOList = dbTestCaseDO.getCsvList();
            if (!CollectionUtils.isEmpty(csvDOList)) {
                csvDOList.forEach(csvDO ->  {
                    log.info("更新用例同步更新CSV文件描述: {}", testCaseParam.getName());
                    CsvVO csvVO = BeanConverter.doSingle(csvDO, CsvVO.class);
                    csvVO.setId(csvDO.getId());
                    csvVO.setDescription(testCaseParam.getName());
                    csvService.updateCsv(csvVO, userVO);
                });
            }
        }

        TestCaseDO testCaseDO = BeanConverter.doSingle(testCaseParam, TestCaseDO.class);
        crudEntity.updateT(testCaseDO, userVO);
        log.info("更新用例: {}", JSON.toJSONString(testCaseDO));
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
    public TestCaseFullVO getFull(Long id) {
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

    @Transactional
    @Override
    public Boolean debugTestCase(Long id, UserVO userVO) {
            /** 调试用例，直接在Master节点通过jmeter调用，而不再 */
            TestCaseDO testCaseDO = testCaseMapper.getById(id);

            /** 查找用例关联的脚本 */
            JmxDO jmxDO = testCaseDO.getJmxDO();
            if (ObjectUtils.isEmpty(jmxDO)) {
                throw new MysteriousException(ResponseCodeEnum.JMX_NOT_EXIST);
            }

            if (!ObjectUtils.isEmpty(userVO)) {
                crudEntity.updateT(testCaseDO, userVO);
                testCaseMapper.update(testCaseDO);
            }

            /** 调试执行的是调试脚本： debug_xxx.jmx */
            String jmxFilePath = jmxDO.getJmxDir() + "debug_" + jmxDO.getDstName();
            log.info("调试脚本: {}", jmxFilePath);

            String currentTime = timeUtil.getCurrentTime();
            /**  创建用例执行报告目录, 区分时间 */
            String reportDir = testCaseDO.getTestCaseDir() + "report" + File.separator + currentTime + File.separator;
            fileUtils.mkDir(reportDir);

            /** jtl文件目录 */
            String jtlDir = reportDir + "jtl" + File.separator;
            fileUtils.mkDir(jtlDir);
            /** jtl文件指定运行 */
            String jtlFile = testCaseDO.getName() + ".xml";
            String jtlFilePath = jtlDir + jtlFile;

            /** 数据报告存放的目录 */
            String reportFilePath = reportDir + "data" + File.separator;
            fileUtils.mkDir(reportFilePath);

            /** jmeter.log文件和目录 */
            String jmeterLogDir = reportDir + "log" + File.separator;
            fileUtils.mkDir(jmeterLogDir);
            /** log文件 */
            String jmeterLogFile = "jmeter_" + currentTime + ".log";
            String jmeterLogFilePath = jmeterLogDir + jmeterLogFile;

            ReportVO reportVO = new ReportVO();
            reportVO.setTestCaseId(testCaseDO.getId());
            reportVO.setJmeterLogFilePath(jmeterLogFilePath);
            reportVO.setName(testCaseDO.getName());
            reportVO.setExecType(ExecTypeEnum.DEBUG.getType());
            reportVO.setDescription("【" + currentTime + "】" + testCaseDO.getDescription());
            /** 调试用例，路径设置为保存返回结果responseData 的 xml文件的目录 */
            reportVO.setReportDir(jtlDir);

            /** 本地直接执行JMX压测脚本 */
            /** 本地单节点执行脚本 */

            /** jmeter可执行程序目录 */
            String masterJmeterBinHome = configService.getValue(JMeterUtil.MASTER_JMETER_BIN_HOME);
            String masterJmeterApplication = masterJmeterBinHome + File.separator + "jmeter";

            /** 可执行程序 */
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

            /** 直接master节点调试 */

            /** 运行中 */
            testCaseDO.setStatus(TestCaseStatus.RUN_ING.getCode());
            testCaseMapper.update(testCaseDO);

            /** 调试，报告还是要入库，因为根据具体报告才能查看返回日志 */
            log.info("新增测试报告: {}", JSON.toJSONString(reportVO, true));
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
        /** slave节点数据库是enable状态，但是机器挂了 */
        for (NodeVO nodeVO : enableNodeList) {
            try {
                pingUtil.ping(nodeVO.getHost());
            } catch (IOException e) {
                throw new MysteriousException(ResponseCodeEnum.NODE_CANNOT_CONNECT);
            }
        }

        TestCaseDO testCaseDO = testCaseMapper.getById(id);

        if (TestCaseStatus.RUN_ING.getCode().equals(testCaseDO.getStatus())) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_IS_RUNNING);
        }
        /** 查找用例关联的脚本 */
        JmxVO jmxVO = jmxService.getByTestCaseId(id);
        if (ObjectUtils.isEmpty(jmxVO)) {
            throw new MysteriousException(ResponseCodeEnum.JMX_NOT_EXIST);
        }

        crudEntity.updateT(testCaseDO, userVO);
        if (!ObjectUtils.isEmpty(userVO)) {
            testCaseMapper.update(testCaseDO);
        }

        /** 已有用例在执行 */
        /** 用例执行排队里，队列里存用例id */
        List<TestCaseDO> runningTestCaseList =
                testCaseMapper.getTestCaseListByStatus(TestCaseStatus.RUN_ING.getCode());
        if (!CollectionUtils.isEmpty(runningTestCaseList)) {
            log.info("已有其他用例正在压测,加入队列,用例id={}", id);
            /**  用例排队key */
            if (redisService.listContainsCaseId(id)) {
                log.warn("队列中已存在该用例， testCaseId: {}", id);
                return true;
            }
            /** 排队的用例，更新当前用例状态，等待执行 */
            testCaseDO.setStatus(TestCaseStatus.RUN_WAITING.getCode());
            testCaseMapper.update(testCaseDO);
            redisService.pushCaseIdToList(id);
            return true;
        }

        String jmxFilePath = jmxVO.getJmxDir() + jmxVO.getDstName();

        String currentTime = timeUtil.getCurrentTime();
        /**  创建用例执行报告目录, 区分时间 */
        String reportDir = testCaseDO.getTestCaseDir() + "report" + File.separator + currentTime + File.separator;
        fileUtils.mkDir(reportDir);

        /** jtl文件目录 */
        String jtlDir = reportDir + "jtl"+ File.separator ;
        fileUtils.mkDir(jtlDir);
        /** jtl文件指定运行 */
        String jtlFile = testCaseDO.getName() + ".jtl";
        String jtlFilePath = jtlDir + jtlFile;

        /** 数据报告存放的目录 */
        String reportFilePath = reportDir + "data" + File.separator;
        fileUtils.mkDir(reportFilePath);

        /** jmeter.log文件和目录 */
        String jmeterLogDir = reportDir + "log" + File.separator;
        fileUtils.mkDir(jmeterLogDir);
        /** log文件 */
        String jmeterLogFile = "jmeter_" + currentTime + ".log";
        String jmeterLogFilePath = jmeterLogDir + jmeterLogFile;

        ReportVO reportVO = new ReportVO();
        reportVO.setTestCaseId(testCaseDO.getId());
        /** jtl文件路径存了没啥用，改为保存jmeter.log的路径 */
        reportVO.setJmeterLogFilePath(jmeterLogFilePath);
        reportVO.setName(testCaseDO.getName());
        reportVO.setExecType(ExecTypeEnum.EXEC.getType());
        reportVO.setDescription("【" + currentTime + "】" + testCaseDO.getDescription());
        reportVO.setReportDir(reportFilePath);

        /** 本地直接执行JMX压测脚本 */
        /** 本地单节点执行脚本 */

        /** jmeter可执行程序目录 */
        String masterJmeterBinHome = configService.getValue(JMeterUtil.MASTER_JMETER_BIN_HOME);
        String masterJmeterApplication = masterJmeterBinHome + File.separator + "jmeter";

        /** 可执行程序 */
        CommandLine commandLine = new CommandLine(masterJmeterApplication);
        commandLine.addArgument("-n");
        commandLine.addArgument("-t");
        commandLine.addArgument(jmxFilePath);
        /** 查看是否有启用的slave节点, 如果有指定-R进行分布式压测 */
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

        /** 在执行JMX脚本之前，需要更新jmeter.properties里修改JAR包的路径，用例维度 */
        /** csv文件，关联的只是jmx脚本，所以上传后就可以修改脚本；jar包依赖配置文件，不能上传了就修改，
         * 因为假如A用户上传了jar包，而改掉了jar配置，而此时B先运行了另一个用例，会报错，因此在此时执行
         * 用例的时候，根据用例来修改jar配置，肯定不会错
         * */

        /** 修改master和slave节点配置文件里jar包配置 */
        /** 新逻辑，上传jar的时候，直接jmx里修改classpath */
        //jarService.updateJarDependency(testCaseDO.getTestCaseDir());

        /** 运行中 */
        /** 因为执行用例会排队；而从startCaseFromRedis方法里执行用例，没法传入SSO，因此执行用例就不更新SSO相关信息了 */
        testCaseDO.setStatus(TestCaseStatus.RUN_ING.getCode());
        testCaseMapper.update(testCaseDO);

        /** 报告 */
        Long reportId = reportService.addReport(reportVO, userVO);
        log.info("新增测试报告: {}", JSON.toJSONString(reportVO, true));

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
        /** jmeter可执行程序目录, shutdown.sh */
        String masterJmeterBinHome = configService.getValue(JMeterUtil.MASTER_JMETER_BIN_HOME);
        String jmxStopScriptPath = masterJmeterBinHome + File.separator + "shutdown.sh";

        CommandLine commandLine = new CommandLine(jmxStopScriptPath);
        /** 不管是master单节点压测，还是分布式压测，脚本的执行都是在master节点，因此只需要master节点执行shutdown.sh */
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

        TestCaseFullVO testCaseFullVO = getFull(testCaseId);
        /** 假如有新增slave节点，需要将用例的所有依赖同步到新的节点上 */
        /** jmx只需要存在于master节点上，所以不需要同步 */
        List<CsvVO> csvVOList = testCaseFullVO.getCsvVOList();
        if (!CollectionUtils.isEmpty(csvVOList)) {
            for (CsvVO csvVO : csvVOList) {
                String csvDir = csvVO.getCsvDir();
                String csvFilePath = csvDir + csvVO.getSrcName();
                ssh.scpFile(csvFilePath, csvDir);
            }
            log.info("csv文件同步结束");
        }

        List<JarVO> jarVOList = testCaseFullVO.getJarVOList();
        if (!CollectionUtils.isEmpty(jarVOList)) {
            for (JarVO jarVO : jarVOList) {
                String jarDir = jarVO.getJarDir();
                String jarFilePath = jarDir + jarVO.getSrcName();
                ssh.scpFile(jarFilePath, jarDir);
            }
            log.info("jar文件同步结束");
        }
    }
}
