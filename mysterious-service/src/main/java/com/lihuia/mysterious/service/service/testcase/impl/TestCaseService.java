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
import com.lihuia.mysterious.core.entity.node.NodeDO;
import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.mapper.testcase.TestCaseMapper;
import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.report.ReportVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseFullVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseQuery;
import com.lihuia.mysterious.core.vo.testcase.TestCaseVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.enums.ExecTypeEnum;
import com.lihuia.mysterious.service.enums.NodeStatusEnum;
import com.lihuia.mysterious.service.enums.TestCaseStatus;
import com.lihuia.mysterious.service.redis.TestCaseRedisService;
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
    private TestCaseRedisService testCaseRedisService;

    @Autowired
    private CRUDEntity<TestCaseDO> crudEntity;

    private void checkTestCaseParam(TestCaseVO testCaseVO) {
        if (ObjectUtils.isEmpty(testCaseVO)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        if (ObjectUtils.isEmpty(testCaseVO.getId())) {
            throw new MysteriousException(ResponseCodeEnum.PARAM_MISSING);
        }
    }

    @Transactional
    @Override
    public Long addTestCase(TestCaseVO testCaseVO, UserVO userVO) {
        if (StringUtils.isEmpty(testCaseVO.getName())
                || testCaseVO.getName().contains(" ")
                || testCaseVO.getName().contains("#")) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_NAME_ERROR);
        }
        if (testCaseMapper.getTestCaseByName(testCaseVO.getName()) != null) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_IS_EXIST);
        }
        /** master节点用例，jmx，jar，csv，报告目录 */
        String mysteriousHomePath = configService.getValue(JMeterUtil.MYSTERIOUS_DATA_HOME);
        String masterTestCasePath = mysteriousHomePath + File.separator
                + testCaseVO.getName() + "_" + timeUtil.getCurrentTime() + File.separator;
        /** 用例名目录，带当前时间，免得用户修改了用例名，可以根据createTime很容易定位到目录 */
        TestCaseDO testCaseDO = BeanConverter.doSingle(testCaseVO, TestCaseDO.class);
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
    public Boolean updateTestCase(TestCaseVO testCaseVO, UserVO userVO) {
        checkTestCaseParam(testCaseVO);

        TestCaseDO dbTestCaseDO = testCaseMapper.getById(testCaseVO.getId());
        if (ObjectUtils.isEmpty(dbTestCaseDO)) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_NOT_EXIST);
        }
        if (StringUtils.isEmpty(testCaseVO.getName())
                || testCaseVO.getName().contains(" ")
                || testCaseVO.getName().contains("#")) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_NAME_ERROR);
        }
        /** 如果用例名有变, 同步更新jmx，jar，csv描述 */
        if (!dbTestCaseDO.getName().equals(testCaseVO.getName())) {
            log.info("同步修改");
            JmxDO jmxDO = dbTestCaseDO.getJmxDO();
            if (jmxDO != null) {
                log.info("更新用例同步更新JMX脚本描述: {}", testCaseVO.getName());
                JmxVO jmxVO = BeanConverter.doSingle(jmxDO, JmxVO.class);
                jmxVO.setId(jmxDO.getId());
                jmxVO.setDescription(testCaseVO.getName());
                jmxService.updateJmx(jmxVO, userVO);
            }
            List<JarDO> jarDOList = dbTestCaseDO.getJarList();
            if (!CollectionUtils.isEmpty(jarDOList)) {
                jarDOList.forEach(jarDO -> {
                    log.info("更新用例同步更新JAR依赖包描述: {}", testCaseVO.getName());
                    JarVO jarVO = BeanConverter.doSingle(jarDO, JarVO.class);
                    jarVO.setId(jarDO.getId());
                    jarVO.setDescription(testCaseVO.getName());
                    jarService.updateJar(jarVO, userVO);
                });
            }
            List<CsvDO> csvDOList = dbTestCaseDO.getCsvList();
            if (!CollectionUtils.isEmpty(csvDOList)) {
                csvDOList.forEach(csvDO ->  {
                    log.info("更新用例同步更新CSV文件描述: {}", testCaseVO.getName());
                    CsvVO csvVO = BeanConverter.doSingle(csvDO, CsvVO.class);
                    csvVO.setId(csvDO.getId());
                    csvVO.setDescription(testCaseVO.getName());
                    csvService.updateCsv(csvVO, userVO);
                });
            }
        }

        TestCaseDO testCaseDO = BeanConverter.doSingle(testCaseVO, TestCaseDO.class);
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

    @Override
    public Boolean debugTestCase(Long id, UserVO userVO) {
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

        TestCaseFullVO testCaseFullVO = getFull(id);

        if (TestCaseStatus.RUN_ING.getCode().equals(testCaseFullVO.getStatus())) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_IS_RUNNING);
        }
        /** 查找用例关联的脚本 */
        JmxVO jmxVO = testCaseFullVO.getJmxVO();
        if (ObjectUtils.isEmpty(jmxVO)) {
            throw new MysteriousException(ResponseCodeEnum.JMX_NOT_EXIST);
        }

        TestCaseDO testCaseDO = BeanConverter.doSingle(testCaseFullVO, TestCaseDO.class);
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
            if (testCaseRedisService.listContainsCaseId(id)) {
                log.warn("队列中已存在该用例， testCaseId: {}", id);
                return true;
            }
            /** 排队的用例，更新当前用例状态，等待执行 */
            testCaseDO.setStatus(TestCaseStatus.RUN_WAITING.getCode());
            testCaseMapper.update(testCaseDO);
            testCaseRedisService.pushCaseIdToList(id);
            return true;
        }

        String jmxFilePath = jmxDO.getTestCaseJmxDir() + jmxDO.getDstName();

        String currentTime = MysteriousTimeUtil.getCurrentTime();
        /**  创建用例执行报告目录, 区分时间 */
        String reportDir = testCaseDO.getTestCaseDir() + "report/" + currentTime + "/";
        fileUtils.mkDir(reportDir);

        /** jtl文件目录 */
        String jtlDir = reportDir + "jtl/";
        fileUtils.mkDir(jtlDir);
        /** jtl文件指定运行 */
        String jtlFile = testCaseDO.getName() + ".jtl";
        String jtlFilePath = jtlDir + jtlFile;

        /** 数据报告存放的目录 */
        String reportFilePath = reportDir + "data/";
        fileUtils.mkDir(reportFilePath);

        /** jmeter.log文件和目录 */
        String jmeterLogDir = reportDir + "log/";
        fileUtils.mkDir(jmeterLogDir);
        /** log文件 */
        String jmeterLogFile = "jmeter_" + currentTime + ".log";
        String jmeterLogFilePath = jmeterLogDir + jmeterLogFile;

        ReportDO reportDO = new ReportDO();
        reportDO.setTestCaseId(testCaseDO.getId());
        //reportDO.setJtlFile(jtlFilePath);
        /** jtl文件路径存了没啥用，改为保存jmeter.log的路径 */
        reportDO.setJmeterLogFilePath(jmeterLogFilePath);
        reportDO.setName(testCaseDO.getName());
        reportDO.setExecType(ExecTypeEnum.EXEC.getType());
        reportDO.setDescription("【" + currentTime + "】" + testCaseDO.getDescription());
        reportDO.setReportDir(reportFilePath);

        /** 本地直接执行JMX压测脚本 */
        /** 本地单节点执行脚本 */

        /** jmeter可执行程序目录 */
        String masterJmeterHomeBin = configService.getValue(JMeterUtil.MASTER_JMETER_HOME_BIN);
        String masterJmeterApplication = masterJmeterHomeBin + "/jmeter";

        /** 可执行程序 */
        CommandLine commandLine = new CommandLine(masterJmeterApplication);
        commandLine.addArgument("-n");
        commandLine.addArgument("-t");
        commandLine.addArgument(jmxFilePath);
        /** 查看是否有启用的slave节点, 如果有指定-R进行分布式压测 */
        //List<NodeDO> enableNodeList = nodeService.getEnableNodeList();
        if (!CollectionUtils.isEmpty(enableNodeList)) {
            commandLine.addArgument("-R");
            String slaveNodes = enableNodeList.stream().map(nodeDO -> nodeDO.getHost()).collect(Collectors.joining(","));
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
        //crudEntityTestCase.initUpdateT(testCaseDO);
        testCaseDO.setStatus(TestCaseStatus.RUN_ING.getCode());
        testCaseMapper.update(testCaseDO);

        /** 报告 */
        reportService.addReport(reportDO, userVO);
        log.info("新增测试报告: {}", JSON.toJSONString(reportDO, true));

        log.info("[runTestCase]commandLine: {}", commandLine.toString()
                .replace(",", "").replace("[", "").replace("]", ""));

        jmxService.runJmx(commandLine, testCaseDO, reportDO, userVO);
        return true;
    }

    @Override
    public Boolean stopTestCase(Long id, UserVO userVO) {
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
        return null;
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
