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
import com.lihuia.mysterious.core.vo.testcase.TestCaseFullVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseQuery;
import com.lihuia.mysterious.core.vo.testcase.TestCaseVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.enums.NodeStatusEnum;
import com.lihuia.mysterious.service.enums.TestCaseStatus;
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
    private MysteriousFileUtils fileUtils;

    @Autowired
    private MysteriousTimeUtil timeUtil;

    @Autowired
    private PingUtil pingUtil;

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
        return null;
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
