package com.lihuia.mysterious.service.service.jmx.impl;

import com.alibaba.fastjson.JSON;
import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.io.MysteriousFileUtils;
import com.lihuia.mysterious.common.jmeter.JMeterUtil;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.mapper.jmx.JmxMapper;
import com.lihuia.mysterious.core.mapper.report.ReportMapper;
import com.lihuia.mysterious.core.mapper.testcase.TestCaseMapper;
import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.jmx.JmxParam;
import com.lihuia.mysterious.core.vo.jmx.JmxQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseFullVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.enums.JMeterScriptEnum;
import com.lihuia.mysterious.service.enums.TestCaseStatus;
import com.lihuia.mysterious.service.handler.dto.ResultDTO;
import com.lihuia.mysterious.service.handler.result.DebugResultHandler;
import com.lihuia.mysterious.service.handler.result.ExecuteResultHandler;
import com.lihuia.mysterious.service.handler.result.ResultHandler;
import com.lihuia.mysterious.service.handler.result.StopResultHandler;
import com.lihuia.mysterious.service.redis.RedisService;
import com.lihuia.mysterious.service.service.config.IConfigService;
import com.lihuia.mysterious.service.service.csv.impl.CsvService;
import com.lihuia.mysterious.service.service.jar.impl.JarService;
import com.lihuia.mysterious.service.service.jmx.IJmxService;
import com.lihuia.mysterious.service.service.testcase.ITestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lihuia.com
 * @date 2022/4/2 11:26 AM
 */

@Slf4j
@Service
public class JmxService implements IJmxService {

    @Autowired
    private IConfigService configService;

    @Autowired
    private ITestCaseService testCaseService;

    @Autowired
    private JmxMapper jmxMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private CRUDEntity<JmxDO> crudEntity;

    @Autowired
    private MysteriousFileUtils fileUtils;

    @Autowired
    private JMeterUtil jMeterUtil;

    @Autowired
    private JarService jarService;

    @Autowired
    private CsvService csvService;

    @Autowired
    private RedisService redisService;

    private void checkJmxParam(JmxVO jmxVO) {
        if (ObjectUtils.isEmpty(jmxVO)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        if (ObjectUtils.isEmpty(jmxVO.getId())) {
            throw new MysteriousException(ResponseCodeEnum.PARAM_MISSING);
        }
        if (ObjectUtils.isEmpty(jmxMapper.getById(jmxVO.getId()))) {
            throw new MysteriousException(ResponseCodeEnum.JMX_NOT_EXIST);
        }
    }

    @Transactional
    @Override
    public Boolean uploadJmx(Long testCaseId, MultipartFile jmxFile, UserVO userVO) {
        TestCaseFullVO testCaseFullVO = testCaseService.getFullVO(testCaseId);
        if (!ObjectUtils.isEmpty(testCaseFullVO.getJmxVO())) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_HAS_JMX);
        }
        log.info("jmxFile: {}", jmxFile);

        /** ?????????jmx?????????????????????*/
        String srcName = jmxFile.getOriginalFilename();
        if (StringUtils.isEmpty(srcName) || srcName.contains(" ")) {
            throw new MysteriousException(ResponseCodeEnum.JMX_NAME_ERROR);
        }
        /** ???????????????????????????????????????jmx????????? */
        String dstName = srcName;

        /** ????????????????????? ??????????????????????????????*/
        String jmxDir = testCaseFullVO.getTestCaseDir() + "jmx/";
        /** ??????????????????????????? */
        String jmxFilePath = jmxDir + dstName;

        /** ??????????????????name + path????????????????????? */
        /** JMX???????????????1???1????????????????????????????????????????????????????????????????????????
         List<JmxDO> existJmxDOList = jmxMapper.getExistJmxList(testCaseId, dstName, jmxDir);
         if (null == existJmxDOList || existJmxDOList.size() > 0) {
         }
         */

        /** ??????jmx????????? */
        JmxDO jmxDO = new JmxDO();
        jmxDO.setSrcName(srcName);
        jmxDO.setDstName(dstName);
        jmxDO.setDescription(testCaseFullVO.getName());
        jmxDO.setJmxDir(jmxDir);
        jmxDO.setTestCaseId(testCaseId);
        /** ????????????????????? */
        jmxDO.setJmeterScriptType(JMeterScriptEnum.UPLOAD_JMX.getCode());
        log.info("??????JMX: {}", JSON.toJSONString(jmxDO, true));
        crudEntity.addT(jmxDO, userVO);
        jmxMapper.add(jmxDO);

        /** ??????jmx?????? */
        fileUtils.uploadFile(jmxFilePath, jmxFile);

        /** ?????????jmx?????????TestPlan.user_define_classpath?????????????????????jar?????? */
        /** writeXML?????????????????????????????????????????????????????????????????? */
        //jmeter.write(jmxFilePath, jmeter.read(jmxFilePath, ".*TestPlan.user_define_classpath\"(/>)", "></stringProp>"));


        /** ???jmx?????????????????????????????????????????????????????????????????? */
        String debugJmxFilePath = jmxDir + "debug_" + dstName;
        fileUtils.copyFile(jmxFilePath, debugJmxFilePath);

        /** ????????????????????????debug????????????????????????????????????1???????????????????????? */
        jMeterUtil.updateDebugThread(debugJmxFilePath);
        return true;
    }

    @Override
    public Boolean updateJmx(JmxVO jmxVO, UserVO userVO) {
        checkJmxParam(jmxVO);
        JmxDO jmxDO = BeanConverter.doSingle(jmxVO, JmxDO.class);
        jmxDO.setId(jmxVO.getId());
        crudEntity.updateT(jmxDO, userVO);
        return jmxMapper.update(jmxDO) > 0;
    }

    @Transactional
    @Override
    public Boolean deleteJmx(Long id) {
        JmxDO jmxDO = jmxMapper.getById(id);
        if (ObjectUtils.isEmpty(jmxDO)) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        List<JarVO> jarVOList = jarService.getByTestCaseId(jmxDO.getTestCaseId());
        if (!CollectionUtils.isEmpty(jarVOList)) {
            throw new MysteriousException(ResponseCodeEnum.JMX_HAS_JAR);
        }
        List<CsvVO> csvVOList = csvService.getByTestCaseId(jmxDO.getTestCaseId());
        if (!CollectionUtils.isEmpty(csvVOList)) {
            throw new MysteriousException(ResponseCodeEnum.JMX_HAS_CSV);
        }

        log.info("??????JMX: {}", JSON.toJSONString(jmxDO));
        jmxMapper.delete(id);
        /** Jmx????????????????????????master???????????????????????? */
        log.info("??????JMX??????: {}", jmxDO.getJmxDir());
        fileUtils.rmFile(jmxDO.getJmxDir());

        return true;
    }

    @Override
    public JmxVO getJmxVO(Long id) {
        JmxDO jmxDO = jmxMapper.getById(id);
        if (ObjectUtils.isEmpty(jmxDO)) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        JmxVO jmxVO = BeanConverter.doSingle(jmxDO, JmxVO.class);
        jmxVO.setId(jmxDO.getId());
        return jmxVO;
    }

    @Override
    public PageVO<JmxVO> getJmxList(JmxQuery query) {
        PageVO<JmxVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(query.getPage(), query.getSize());
        Integer total = jmxMapper.getJmxCount(query.getSrcName(), query.getTestCaseId(), query.getCreatorId());
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<JmxDO> jmxDOList =
                    jmxMapper.getJmxList(query.getSrcName(), query.getTestCaseId(), query.getCreatorId(), offset, query.getSize());
            if (!CollectionUtils.isEmpty(jmxDOList)) {
                pageVO.setList(jmxDOList.stream().map(jmxDO -> {
                    JmxVO jmxVO = BeanConverter.doSingle(jmxDO, JmxVO.class);
                    jmxVO.setId(jmxDO.getId());
                    return jmxVO;
                }).collect(Collectors.toList()));
            }
        }
        return pageVO;
    }

    @Override
    public Boolean runJmx(CommandLine commandLine, Long testCaseId, Long reportId, UserVO userVO) {
        DefaultExecutor executor = new DefaultExecutor();

        TestCaseDO testCaseDO = testCaseMapper.getById(testCaseId);
        ReportDO reportDO = reportMapper.getById(reportId);

        try {
            /** ????????????????????????????????????????????????????????? */
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);

            /** ???????????? */
            executor.setStreamHandler(streamHandler);

            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setTestCaseDO(testCaseDO);
            resultDTO.setReportDO(reportDO);
            resultDTO.setTestCaseMapper(testCaseMapper);
            resultDTO.setReportMapper(reportMapper);
            resultDTO.setOutputStream(outputStream);
            resultDTO.setErrorStream(errorStream);
            resultDTO.setRedisService(redisService);
            ResultHandler resultHandler = new ExecuteResultHandler(resultDTO);

            log.info("??????JMX??????: {}", commandLine);
            executor.execute(commandLine, resultHandler);
        } catch (IOException e) {
            log.info("????????????, ??????????????????", e);
            testCaseDO.setStatus(TestCaseStatus.RUN_FAILED.getCode());
            testCaseMapper.update(testCaseDO);
//            if (null != reportDO) {
//                reportService.addReport(reportDO);
//            }
            throw new MysteriousException(ResponseCodeEnum.RUN_JMX_ERROR);
        }
        return true;
    }

    @Override
    public Boolean debugJmx(CommandLine commandLine, Long testCaseId, Long reportId, UserVO userVO) {
        DefaultExecutor executor = new DefaultExecutor();

        TestCaseDO testCaseDO = testCaseMapper.getById(testCaseId);
        ReportDO reportDO = reportMapper.getById(reportId);

        try {
            /** ????????????????????????????????????????????????????????? */
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);

            /** ???????????? */
            executor.setStreamHandler(streamHandler);

            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setTestCaseDO(testCaseDO);
            resultDTO.setReportDO(reportDO);
            resultDTO.setTestCaseMapper(testCaseMapper);
            resultDTO.setReportMapper(reportMapper);
            resultDTO.setOutputStream(outputStream);
            resultDTO.setErrorStream(errorStream);
            resultDTO.setRedisService(redisService);
            ResultHandler resultHandler = new DebugResultHandler(resultDTO);

            log.info("??????JMX??????: {}", commandLine);
            executor.execute(commandLine, resultHandler);
        } catch (Exception e) {
            throw new MysteriousException(ResponseCodeEnum.SCRIPT_DEBUG_ERROR);
        }
        return true;
    }

    @Override
    public Boolean stopJmx(CommandLine commandLine, Long testCaseId, UserVO userVO) {
        DefaultExecutor executor = new DefaultExecutor();

        TestCaseDO testCaseDO = testCaseMapper.getById(testCaseId);

        try {
            /** ????????????????????????????????????????????????????????? */
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);

            /** ???????????? */
            executor.setStreamHandler(streamHandler);

            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setTestCaseDO(testCaseDO);
            resultDTO.setTestCaseMapper(testCaseMapper);
            resultDTO.setOutputStream(outputStream);
            resultDTO.setErrorStream(errorStream);
            ResultHandler resultHandler = new StopResultHandler(resultDTO);

            log.info("??????JMX??????: {}", commandLine);
            executor.execute(commandLine, resultHandler);
        } catch (Exception e) {
            throw new MysteriousException(ResponseCodeEnum.SCRIPT_STOP_ERROR);
        }
        return true;
    }

    @Override
    public JmxVO getByTestCaseId(Long testCaseId) {
        JmxDO jmxDO = jmxMapper.getByTestCaseId(testCaseId);
        if (!ObjectUtils.isEmpty(jmxDO)) {
            JmxVO jmxVO = BeanConverter.doSingle(jmxDO, JmxVO.class);
            jmxVO.setId(jmxDO.getId());
            return jmxVO;
        }
        return null;
    }

    @Override
    public JmxDO getJmxDO(Long testCaseId) {
        return jmxMapper.getByTestCaseId(testCaseId);
    }

    @Override
    public Boolean addOnlineJmx(JmxVO jmxVO) {
        return null;
    }

    @Override
    public JmxVO getOnlineJmx(Long id) {
        return null;
    }

    @Override
    public Boolean updateOnlineJmx(JmxVO jmxVO) {
        return null;
    }

    @Override
    public Boolean forceDelete(Long id) {
        return null;
    }
}
