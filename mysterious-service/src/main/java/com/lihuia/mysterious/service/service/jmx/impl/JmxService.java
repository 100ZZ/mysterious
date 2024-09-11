package com.lihuia.mysterious.service.service.jmx.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.io.MysteriousFileUtils;
import com.lihuia.mysterious.common.jmeter.JMeterUtil;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpDO;
import com.lihuia.mysterious.core.entity.jmx.thread.ConcurrencyThreadGroupDO;
import com.lihuia.mysterious.core.entity.jmx.thread.SteppingThreadGroupDO;
import com.lihuia.mysterious.core.entity.jmx.thread.ThreadGroupDO;
import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.mapper.jmx.JmxMapper;
import com.lihuia.mysterious.core.mapper.report.ReportMapper;
import com.lihuia.mysterious.core.mapper.testcase.TestCaseMapper;
import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.jmx.JmxQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.jmx.sample.dubbo.DubboVO;
import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpHeaderVO;
import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpParamVO;
import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpVO;
import com.lihuia.mysterious.core.vo.jmx.sample.java.JavaParamVO;
import com.lihuia.mysterious.core.vo.jmx.sample.java.JavaVO;
import com.lihuia.mysterious.core.vo.jmx.thread.ConcurrencyThreadGroupVO;
import com.lihuia.mysterious.core.vo.jmx.thread.SteppingThreadGroupVO;
import com.lihuia.mysterious.core.vo.jmx.thread.ThreadGroupVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseFullVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.enums.JMeterSampleEnum;
import com.lihuia.mysterious.service.enums.JMeterScriptEnum;
import com.lihuia.mysterious.service.enums.JMeterThreadsEnum;
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
import com.lihuia.mysterious.service.service.jmeter.JMeterXMLService;
import com.lihuia.mysterious.service.service.jmx.IJmxService;
import com.lihuia.mysterious.service.service.jmx.sample.dubbo.IDubboService;
import com.lihuia.mysterious.service.service.jmx.sample.http.IHttpHeaderService;
import com.lihuia.mysterious.service.service.jmx.sample.http.IHttpParamService;
import com.lihuia.mysterious.service.service.jmx.sample.http.IHttpService;
import com.lihuia.mysterious.service.service.jmx.sample.java.IJavaParamService;
import com.lihuia.mysterious.service.service.jmx.sample.java.IJavaService;
import com.lihuia.mysterious.service.service.jmx.thread.IConcurrencyThreadGroupService;
import com.lihuia.mysterious.service.service.jmx.thread.ISteppingThreadGroupService;
import com.lihuia.mysterious.service.service.jmx.thread.IThreadGroupService;
import com.lihuia.mysterious.service.service.testcase.ITestCaseService;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lihuia.com
 * @date 2023/4/2 11:26 AM
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

    @Autowired
    private JMeterXMLService jmeterXMLService;

    @Autowired
    private IThreadGroupService threadGroupService;

    @Autowired
    private ISteppingThreadGroupService steppingThreadGroupService;

    @Autowired
    private IConcurrencyThreadGroupService concurrencyThreadGroupService;

    @Autowired
    private IHttpService httpService;

    @Autowired
    private IHttpHeaderService httpHeaderService;

    @Autowired
    private IHttpParamService httpParamService;

    @Autowired
    private IDubboService dubboService;

    @Autowired
    private IJavaService javaService;

    @Autowired
    private IJavaParamService javaParamService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /** mongodb保存body */
    private final static String BODY_COLLECTION = "mysterious_jmx_http_body";

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

        /** 上传的jmx文件名，带后缀*/
        String srcName = jmxFile.getOriginalFilename();
        if (ObjectUtils.isEmpty(srcName)
                || !srcName.contains(".jmx")
                || StringUtils.isBlank(srcName)
                || srcName.contains(" ")) {
            throw new MysteriousException(ResponseCodeEnum.JMX_NAME_ERROR);
        }
        /** 上传后保存，以及最终执行的jmx文件名 */
        String dstName = srcName;

        /** 根据用例目录， 组成用例用例完整路径*/
        String jmxDir = testCaseFullVO.getTestCaseDir() + "jmx/";
        /** 用例脚本的完整路径 */
        String jmxFilePath = jmxDir + dstName;

        /** 上传前，校验name + path是否表里已存在 */
        /** JMX和用例只能1：1，前面已经校验了用例关联的唯一性，这里可以不需要
         List<JmxDO> existJmxDOList = jmxMapper.getExistJmxList(testCaseId, dstName, jmxDir);
         if (null == existJmxDOList || existJmxDOList.size() > 0) {
         }
         */

        /** 更新jmx脚本表 */
        JmxDO jmxDO = new JmxDO();
        jmxDO.setSrcName(srcName);
        jmxDO.setDstName(dstName);
        jmxDO.setDescription(testCaseFullVO.getName());
        jmxDO.setJmxDir(jmxDir);
        jmxDO.setTestCaseId(testCaseId);
        /** 改脚本是上传的 */
        jmxDO.setJmeterScriptType(JMeterScriptEnum.UPLOAD_JMX.getCode());
        log.info("新增JMX: {}", JSON.toJSONString(jmxDO, true));
        crudEntity.addT(jmxDO, userVO);
        jmxMapper.add(jmxDO);

        /** 上传jmx文件 */
        fileUtils.uploadFile(jmxFilePath, jmxFile);

        /** 上传了jmx后，给TestPlan.user_define_classpath配置一个默认值jar路径 */
        /** writeXML里会默认设置为自闭和，已修改，因此这里不需要 */
        //jmeter.write(jmxFilePath, jmeter.read(jmxFilePath, ".*TestPlan.user_define_classpath\"(/>)", "></stringProp>"));


        /** 将jmx脚本拷贝一份，修改线程变量，调试执行一次使用 */
        String debugJmxFilePath = jmxDir + "debug_" + dstName;
        fileUtils.copyFile(jmxFilePath, debugJmxFilePath);

        /** 拷贝完之后，修改debug脚本的线程数，全部修改为1，使得只执行一次 */
        /** 这里目前支持三种线程组插件 */
        //线程组插件1：原生默认ThreadGroup
        //线程组插件2：SteppingThreadGroup
        //线程组插件3：bzm里的ConcurrencyThreadGroup
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

        log.info("删除JMX: {}", JSON.toJSONString(jmxDO));
        jmxMapper.delete(id);

        /** 如果是在线编辑的脚本，除了上面，还需要删除关联的所有记录 */
        if (jmxDO.getJmeterScriptType().equals(JMeterScriptEnum.ONLINE_JMX.getCode())) {

            if (jmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.THREAD_GROUP.getCode())) {
                threadGroupService.deleteThreadGroup(threadGroupService.getByJmxId(id).getId());
            } else if (jmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
                steppingThreadGroupService.deleteSteppingThreadGroup(steppingThreadGroupService.getByJmxId(id).getId());
            } else if (jmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
                concurrencyThreadGroupService.deleteConcurrencyThreadGroup(concurrencyThreadGroupService.getByJmxId(id).getId());
            } else {
                throw new MysteriousException("deleteJmx, 线程组类型异常, 请确认");
            }

            if (jmxDO.getJmeterSampleType().equals(JMeterSampleEnum.HTTP_REQUEST.getCode())) {
                HttpVO httpVO = httpService.getByJmxId(id);
                httpService.deleteHttp(httpVO.getId());
                /** 删除mongo里的body */
                Query query = new Query(Criteria
                        .where("httpId").is(httpVO.getId())
                        .and("jmxId").is(httpVO.getJmxId())
                        .and("testCaseId").is(httpVO.getTestCaseId()));
                log.info("Mongo remove body, httpId:{}, jmxId:{}, testCaseId:{}", httpVO.getId(), httpVO.getJmxId(), httpVO.getTestCaseId());
                mongoTemplate.remove(query, BODY_COLLECTION);
                List<HttpHeaderVO> headerVOList = httpHeaderService.getListByHttpId(httpVO.getId());
                if (!CollectionUtils.isEmpty(headerVOList)) {
                    headerVOList.forEach(httpHeaderVO -> httpHeaderService.deleteHttpHeader(httpHeaderVO.getId()));
                }
                List<HttpParamVO> paramVOList = httpParamService.getListByHttpId(httpVO.getId());
                if (!CollectionUtils.isEmpty(paramVOList)) {
                    paramVOList.forEach(httpParamVO -> httpParamService.deleteHttpParam(httpParamVO.getId()));
                }
            } else if (jmxDO.getJmeterSampleType().equals(JMeterSampleEnum.DUBBO_SAMPLE.getCode())) {

            } else if (jmxDO.getJmeterSampleType().equals(JMeterSampleEnum.JAVA_REQUEST.getCode())) {
                JavaVO javaVO = javaService.getByJmxId(id);
                javaService.deleteJava(javaVO.getId());
                List<JavaParamVO> javaParamVOList = javaParamService.getListByJavaId(javaVO.getId());
                if (!CollectionUtils.isEmpty(javaParamVOList)) {
                    javaParamVOList.forEach(javaParamVO -> javaParamService.deleteJavaParam(javaParamVO.getId()));
                }
            } else {
                throw new MysteriousException("deleteJmx, sample类型异常, 请确认");
            }
        }

        /** Jmx脚本文件只保存在master节点上，删除文件 */
        log.info("删除JMX目录: {}", jmxDO.getJmxDir());
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
        Integer total = jmxMapper.getJmxCount(query.getSrcName(), query.getTestCaseId());
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<JmxDO> jmxDOList =
                    jmxMapper.getJmxList(query.getSrcName(), query.getTestCaseId(), offset, query.getSize());
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
            /** 非阻塞运行脚本命令，不影响正常其它操作 */
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);

            /** 错误输出 */
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

            log.info("执行JMX脚本: {}", commandLine);
            executor.execute(commandLine, resultHandler);
        } catch (IOException e) {
            log.info("执行异常, 更新失败状态", e);
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
            /** 非阻塞运行脚本命令，不影响正常其它操作 */
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);

            /** 错误输出 */
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

            log.info("调试JMX脚本: {}", commandLine);
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
            /** 非阻塞运行脚本命令，不影响正常其它操作 */
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);

            /** 错误输出 */
            executor.setStreamHandler(streamHandler);

            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setTestCaseDO(testCaseDO);
            resultDTO.setTestCaseMapper(testCaseMapper);
            resultDTO.setOutputStream(outputStream);
            resultDTO.setErrorStream(errorStream);
            ResultHandler resultHandler = new StopResultHandler(resultDTO);

            log.info("停止JMX脚本: {}", commandLine);
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

    private void checkJmxName(String name) {
        if (StringUtils.isBlank(name)
                || name.contains(" ")
                || !Pattern.compile("\\.jmx$").matcher(name).find()) {
            throw new MysteriousException("JMX名称以.jmx为后缀, 不能包含空格");
        }
    }

    private void checkThreadGroupVO(ThreadGroupVO threadGroupVO) {
        log.info("校验默认线程组: checkThreadGroupVO");
        if (null == threadGroupVO) {
            throw new MysteriousException("系统异常, 默认线程组类型和数据不匹配");
        }
        if (StringUtils.isBlank(threadGroupVO.getNumThreads())
                || StringUtils.isBlank(threadGroupVO.getRampTime())
                || StringUtils.isBlank(threadGroupVO.getLoops())) {
            throw new MysteriousException("ThreadGroup参数不完整, 请确认");
        }
    }

    private void checkSteppingThreadGroupVO(SteppingThreadGroupVO steppingThreadGroupVO) {
        log.info("校验梯度线程组: checkSteppingThreadGroupVO");
        if (null == steppingThreadGroupVO) {
            throw new MysteriousException("系统异常, 梯度线程组类型和数据不匹配");
        }
        if (StringUtils.isBlank(steppingThreadGroupVO.getNumThreads())
                || StringUtils.isBlank(steppingThreadGroupVO.getFirstWaitForSeconds())
                || StringUtils.isBlank(steppingThreadGroupVO.getThenStartThreads())
                || StringUtils.isBlank(steppingThreadGroupVO.getNextAddThreads())
                || StringUtils.isBlank(steppingThreadGroupVO.getNextAddThreadsEverySeconds())
                || StringUtils.isBlank(steppingThreadGroupVO.getUsingRampUpSeconds())
                || StringUtils.isBlank(steppingThreadGroupVO.getThenHoldLoadForSeconds())
                || StringUtils.isBlank(steppingThreadGroupVO.getFinallyStopThreads())
                || StringUtils.isBlank(steppingThreadGroupVO.getFinallyStopThreadsEverySeconds())) {
            throw new MysteriousException("SteppingThreadGroup参数不完整, 请确认");
        }
    }

    private void checkConcurrencyThreadGroupVO(ConcurrencyThreadGroupVO concurrencyThreadGroupVO) {
        log.info("校验并发线程组: checkConcurrencyThreadGroupVO");
        if (null == concurrencyThreadGroupVO) {
            throw new MysteriousException("系统异常, 并发线程组类型和数据不匹配");
        }
        if (StringUtils.isBlank(concurrencyThreadGroupVO.getTargetConcurrency())
                || StringUtils.isBlank(concurrencyThreadGroupVO.getRampUpTime())
                || StringUtils.isBlank(concurrencyThreadGroupVO.getRampUpStepsCount())
                || StringUtils.isBlank(concurrencyThreadGroupVO.getHoldTargetRateTime())) {
            throw new MysteriousException("ConcurrencyThreadGroup参数不完整, 请确认");
        }
    }

    private boolean checkHttpBodyIsEmpty(String body) {
        if (StringUtils.isBlank(body)) {
            return true;
        }
        JSONObject json;
        try {
            json = JSONObject.parseObject(body);
        } catch (Exception e) {
            throw new MysteriousException("body不是JSON格式, 请确认");
        }
        return json.isEmpty();
    }

    private void checkHttpVO(HttpVO httpVO) {
        log.info("校验HTTP请求: checkHttpVO");
        if (null == httpVO) {
            throw new MysteriousException("系统异常, HTTP请求类型和数据不匹配");
        }
        if (StringUtils.isBlank(httpVO.getMethod())
                || StringUtils.isBlank(httpVO.getProtocol())
                || StringUtils.isBlank(httpVO.getDomain())
                || StringUtils.isBlank(httpVO.getPort())
                || StringUtils.isBlank(httpVO.getPath())
                || StringUtils.isBlank(httpVO.getContentEncoding())) {
            throw new MysteriousException("HTTP请求参数不完整, 请确认");
        }

        /** 所有method都提供header，param，body，自行填写，不需要区分
         if (httpDO.getBody() != null && !httpDO.getMethod().equals(HttpMethodEnum.POST.getMethod())) {
         throw new NPSException("非post请求body非空");
         } else if (httpDO.getBody() == null && httpDO.getMethod().equals(HttpMethodEnum.POST.getMethod())) {
         throw new NPSException("post请求body为空");
         } else if (httpDO.getBody() != null && !CollectionUtils.isEmpty(httpDO.getHttpParamDOList())) {
         throw new NPSException("body和param同时存在, 不合理");
         }
         */
    }

    private void checkDubboVO(DubboVO dubboVO) {

    }

    private void checkJavaVO(JavaVO javaVO) {
        log.info("校验Java请求: checkJavaVO");
        if (null == javaVO) {
            throw new MysteriousException("系统异常, Java请求类型和数据不匹配");
        }
        if (StringUtils.isBlank(javaVO.getJavaRequestClassPath())) {
            throw new MysteriousException("Java请求参数有误, 请确认");
        }
    }

    private void checkJmxVO(JmxVO jmxVO) {

        /** 脚本类型，是在线编辑，直接写死，不用传，不用判断 */
//        if (!jmxDO.getJmeterScriptType().equals(JMeterScriptEnum.ONLINE_JMX.getCode())) {
//            throw new NPSException("非在线编辑脚本, 请确认参数");
//        }
        /** 线程组 */
        if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.THREAD_GROUP.getCode())) {
            checkThreadGroupVO(jmxVO.getThreadGroupVO());
        } else if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
            checkSteppingThreadGroupVO(jmxVO.getSteppingThreadGroupVO());
        } else if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
            checkConcurrencyThreadGroupVO(jmxVO.getConcurrencyThreadGroupVO());
        } else {
            log.info("线程组类型异常, JmeterThreadsType: {}", jmxVO.getJmeterThreadsType());
            throw new MysteriousException("线程组类型异常");
        }

        /** Sample校验 */
        if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.HTTP_REQUEST.getCode())) {
            checkHttpVO(jmxVO.getHttpVO());
        } else if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.DUBBO_SAMPLE.getCode())) {
            checkDubboVO(jmxVO.getDubboVO());
        } else if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.JAVA_REQUEST.getCode())) {
            checkJavaVO(jmxVO.getJavaVO());
        } else {
            log.info("Sample类型异常, JmeterSampleType: {}", jmxVO.getJmeterSampleType());
            throw new MysteriousException("Sample类型异常");
        }
    }

    @Synchronized
    @Transactional
    @Override
    public Boolean addOnlineJmx(JmxVO jmxVO, UserVO userVO) {
        /** jmx名称校验 */
        String srcName = jmxVO.getSrcName();
        checkJmxName(srcName);
        String dstName = srcName;

        /** 用例是否已经关联了jmx脚本, jmxDO里用例ID必填 */
        Long testCaseId = jmxVO.getTestCaseId();
        TestCaseFullVO testCaseFullVO = testCaseService.getFullVO(testCaseId);
        if (null != testCaseFullVO.getJmxVO()) {
            throw new MysteriousException("用例已经关联了jmx脚本");
        }

        log.info("开始校验脚本关联参数...");
        checkJmxVO(jmxVO);
        String baseJmxFilePath = getBaseJmxFilePath(jmxVO);
        log.info("baseJmxFilePath: {}", baseJmxFilePath);

        JmxDO jmxDO = BeanConverter.doSingle(jmxVO, JmxDO.class);

        jmxDO.setSrcName(srcName);
        jmxDO.setDstName(dstName);
        jmxDO.setDescription(testCaseFullVO.getName());
        jmxDO.setJmeterScriptType(JMeterScriptEnum.ONLINE_JMX.getCode());

        /** jmx磁盘保存的路径 */
        String jmxDir = testCaseFullVO.getTestCaseDir() + "jmx/";
        log.info("jmxDir: {}", jmxDir);
        if (fileUtils.isDirectory(jmxDir)) {
            //throw new MysteriousException("jmxDir已存在, 异常");
            /** 这里有个场景，因为磁盘操作没法事务回滚，假如后面报错了，这个目录创建了，下次这里就直接退出了 */
            fileUtils.rmDir(jmxDir);
        }
        /** 上传jmx会自动创建；在线编辑也要创建jmx路径，因为删除的时候，会删除整个jmx目录 */
        fileUtils.mkDir(jmxDir);
        jmxDO.setJmxDir(jmxDir);
        /** 入库jmx表 */
        log.info("入库前 JmxDO: {}", JSON.toJSONString(jmxDO, true));
        crudEntity.addT(jmxDO, userVO);
        jmxMapper.add(jmxDO);

        /** 获取jmxId，后面入库 */
        JmxDO dbJmxDO = jmxMapper.getByTestCaseId(testCaseId);
        log.info("入库后 JmxDO: {}", JSON.toJSONString(dbJmxDO, true));

        if (ObjectUtils.isEmpty(dbJmxDO)) {
            throw new MysteriousException("jmx入库失败: " + srcName);
        }
        Long jmxId = dbJmxDO.getId();

        /** 每种类型，提供不同的base脚本 */
        log.info("baseJmxFilePath: {}", baseJmxFilePath);
        String jmxFilePath = jmxDir + dstName;
        log.info("jmxFilePath: {}", jmxFilePath);
        String debugJmxFilePath = jmxDir + "debug_" + dstName;
        log.info("debugJmxFilePath: {}", debugJmxFilePath);

        /** 准备扩充jmx脚本 */
        jmeterXMLService.init(baseJmxFilePath);

        /** 线程组 */
        if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.THREAD_GROUP.getCode())) {
            ThreadGroupVO threadGroupVO = jmxVO.getThreadGroupVO();
            threadGroupVO.setJmxId(jmxId);
            threadGroupVO.setTestCaseId(jmxDO.getTestCaseId());
            /** 默认线程组入库 */
            threadGroupService.addThreadGroup(threadGroupVO);
            /** 线程组扩充 */
            jmeterXMLService.updateThreadGroup(threadGroupVO);

        } else if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
            SteppingThreadGroupVO steppingThreadGroupVO = jmxVO.getSteppingThreadGroupVO();
            steppingThreadGroupVO.setTestCaseId(testCaseId);
            steppingThreadGroupVO.setJmxId(jmxId);
            /** 梯度线程组入库 */
            steppingThreadGroupService.addSteppingThreadGroup(steppingThreadGroupVO);
            /** todo 梯度线程组扩充 */
            jmeterXMLService.updateSteppingThreadGroup(steppingThreadGroupVO);
        } else if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
            ConcurrencyThreadGroupVO concurrencyThreadGroupVO = jmxVO.getConcurrencyThreadGroupVO();
            concurrencyThreadGroupVO.setTestCaseId(testCaseId);
            concurrencyThreadGroupVO.setJmxId(jmxId);
            /** 线程组入库 */
            concurrencyThreadGroupService.addConcurrencyThreadGroup(concurrencyThreadGroupVO);
            jmeterXMLService.updateConcurrencyThreadGroup(concurrencyThreadGroupVO);
        } else {
            log.warn("系统异常, 线程组类型: {}", jmxVO.getJmeterThreadsType());
            throw new MysteriousException("系统异常, 线程组类型不合法");
        }

        if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.HTTP_REQUEST.getCode())) {
            HttpVO httpVO = jmxVO.getHttpVO();
            httpVO.setTestCaseId(testCaseId);
            httpVO.setJmxId(jmxId);
            httpService.addHttp(httpVO);
            /** 填充http内容 */
            jmeterXMLService.updateHttpSample(httpVO);

            /** 获取httpId，后面入库; 因为一个jmx只有一个http */
            HttpVO newHttpVO = httpService.getByJmxId(jmxId);
            if (ObjectUtils.isEmpty(newHttpVO)) {
                throw new MysteriousException("http入库失败: {}" + srcName);
            }
            Long httpId = newHttpVO.getId();

            /**  body都存在mongodb */
            String body = httpVO.getBody();
            if (!checkHttpBodyIsEmpty(body)) {
                JSONObject json = new JSONObject();
                json.put("body", JSON.parseObject(body));
                json.put("testCaseId", testCaseId);
                json.put("jmxId", jmxId);
                json.put("httpId", httpId);
                log.info("Mongo insert body, httpId:{}, jmxId:{}, testCaseId:{}", httpId, httpVO.getJmxId(), httpVO.getTestCaseId());
                log.info("Mongo insert body, body:{}", JSON.parseObject(body));
                mongoTemplate.insert(json, BODY_COLLECTION);
                /** 修改脚本文件 */
                jmeterXMLService.addHttpBody(body);
            }

            /** 是否有http header */
            List<HttpHeaderVO> headerVOList = httpVO.getHttpHeaderVOList();
            if (!CollectionUtils.isEmpty(headerVOList)) {
                for (HttpHeaderVO headerVO : headerVOList) {
                    if (!CollectionUtils.isEmpty(httpHeaderService.getExistHeaderList(httpId, headerVO.getHeaderKey()))) {
                        throw new MysteriousException("Header: " + headerVO.getHeaderKey() + " 已存在");
                    }
                    headerVO.setTestCaseId(testCaseId);
                    headerVO.setJmxId(jmxId);
                    headerVO.setHttpId(httpId);
                    httpHeaderService.addHttpHeader(headerVO);
                    /** JMX脚本里填充Http header内容 */
                    jmeterXMLService.addHttpHeader(headerVO.getHeaderKey(), headerVO.getHeaderValue());
                }
            }
            /** 是否有http param */
            List<HttpParamVO> paramVOList = httpVO.getHttpParamVOList();
            if (!CollectionUtils.isEmpty(paramVOList)) {
                for (HttpParamVO paramVO : paramVOList) {
                    if (!CollectionUtils.isEmpty(httpParamService.getExistParamList(httpId, paramVO.getParamKey()))) {
                        throw new MysteriousException("HTTP Param: " + paramVO.getParamKey() + " 已存在");
                    }
                    paramVO.setTestCaseId(testCaseId);
                    paramVO.setJmxId(jmxId);
                    paramVO.setHttpId(httpId);
                    httpParamService.addHttpParam(paramVO);
                    /** JMX脚本里填充Http param内容 */
                    jmeterXMLService.addHttpParam(paramVO.getParamKey(), paramVO.getParamValue());
                }
            }

        } else if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.DUBBO_SAMPLE.getCode())) {
            //todo
        } else if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.JAVA_REQUEST.getCode())) {
            JavaVO javaVO = jmxVO.getJavaVO();
            javaVO.setTestCaseId(testCaseId);
            javaVO.setJmxId(jmxId);
            javaService.addJava(javaVO);
            //写XML: classname
            jmeterXMLService.updateJavaRequest(javaVO.getJavaRequestClassPath());

            /** 入库后带ID的JavaDO */
            JavaVO newJavaVO = javaService.getByJmxId(jmxId);
            if (null == newJavaVO) {
                throw new MysteriousException("java入库失败: {}" + srcName);
            }
            Long javaId = newJavaVO.getId();
            List<JavaParamVO> javaParamVOList = javaVO.getJavaParamVOList();
            if (!CollectionUtils.isEmpty(javaParamVOList)) {
                for (JavaParamVO javaParamVO : javaParamVOList) {
                    if (!CollectionUtils.isEmpty(javaParamService.getExistParamList(javaId, javaParamVO.getParamKey()))) {
                        throw new MysteriousException("Java Param: " + javaParamVO.getParamValue() + " 已存在");
                    }
                    javaParamVO.setTestCaseId(testCaseId);
                    javaParamVO.setJmxId(jmxId);
                    javaParamVO.setJavaId(javaId);
                    javaParamService.addJavaParam(javaParamVO);
                    //写XML: param
                    jmeterXMLService.addJavaParam(javaParamVO.getParamKey(), javaParamVO.getParamValue());
                }
            }
        } else {
            log.warn("系统异常, Sample类型: {}", jmxVO.getJmeterSampleType());
            throw new MysteriousException("系统异常, Sample类型不合法");
        }

        /** 将JMX的更改写入指定路径脚本 */
        jmeterXMLService.writeJmxFile(jmxFilePath);

        /** 将jmx脚本拷贝一份，修改线程变量，调试执行一次使用 */
        fileUtils.copyFile(jmxFilePath, debugJmxFilePath);

        /** 拷贝完之后，修改debug脚本的线程数，全部修改为1，使得只执行一次 */
        jMeterUtil.updateDebugThread(debugJmxFilePath);

        return true;
    }

    private String getBaseJmxFilePath(JmxVO jmxVO) {
        StringBuilder baseJmxFileName = new StringBuilder();

        /** 脚本类型，是在线编辑，直接写死，不用传，不用判断 */
//        if (!jmxDO.getJmeterScriptType().equals(JMeterScriptEnum.ONLINE_JMX.getCode())) {
//            throw new NPSException("非在线编辑脚本, 请确认参数");
//        }
        /** 线程组 */
        if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.THREAD_GROUP.getCode())) {
            checkThreadGroupVO(jmxVO.getThreadGroupVO());
            baseJmxFileName.append("thread_group");
        } else if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
            checkSteppingThreadGroupVO(jmxVO.getSteppingThreadGroupVO());
            baseJmxFileName.append("stepping_thread_group");
        } else if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
            checkConcurrencyThreadGroupVO(jmxVO.getConcurrencyThreadGroupVO());
            baseJmxFileName.append("concurrency_thread_group");
        } else {
            log.info("线程组类型异常, JmeterThreadsType: {}", jmxVO.getJmeterThreadsType());
            throw new MysteriousException("线程组类型异常");
        }

        /** Sample校验 */
        if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.HTTP_REQUEST.getCode())) {
            checkHttpVO(jmxVO.getHttpVO());
            baseJmxFileName.append("_http.jmx");
        } else if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.DUBBO_SAMPLE.getCode())) {
            checkDubboVO(jmxVO.getDubboVO());
            baseJmxFileName.append("_dubbo.jmx");
        } else if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.JAVA_REQUEST.getCode())) {
            checkJavaVO(jmxVO.getJavaVO());
            baseJmxFileName.append("_java.jmx");
        } else {
            log.info("Sample类型异常, JmeterSampleType: {}", jmxVO.getJmeterSampleType());
            throw new MysteriousException("Sample类型异常");
        }

        return configService.getValue(JMeterUtil.MASTER_BASE_JMX_FILES_PATH) + "/" + baseJmxFileName.toString();
    }

    @Override
    public JmxVO getOnlineJmx(Long id) {
        JmxDO jmxDO = jmxMapper.getById(id);
        if (null == jmxDO) {
            throw new MysteriousException("脚本不存在: " + id);
        }
        if (!jmxDO.getJmeterScriptType().equals(JMeterScriptEnum.ONLINE_JMX.getCode())) {
            throw new MysteriousException("当前已关联了非在线编写的脚本,无法进行脚本在线操作");
        }
        JmxVO jmxVO = BeanConverter.doSingle(jmxDO, JmxVO.class);
        if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.THREAD_GROUP.getCode())) {
            jmxVO.setThreadGroupVO(threadGroupService.getByJmxId(id));
        } else if (jmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
            jmxVO.setSteppingThreadGroupVO(steppingThreadGroupService.getByJmxId(id));
        } else if (jmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
            jmxVO.setConcurrencyThreadGroupVO(concurrencyThreadGroupService.getByJmxId(id));
        } else {
            throw new MysteriousException("线程组类型异常, 请确认");
        }

        if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.HTTP_REQUEST.getCode())) {
            HttpVO httpVO = httpService.getByJmxId(id);
            httpVO.setHttpHeaderVOList(httpHeaderService.getListByHttpId(httpVO.getId()));
            httpVO.setHttpParamVOList(httpParamService.getListByHttpId(httpVO.getId()));
            /** 从mongodb里捞body */
            Query query = new Query(Criteria
                    .where("httpId").is(httpVO.getId())
                    .and("jmxId").is(httpVO.getJmxId())
                    .and("testCaseId").is(httpVO.getTestCaseId()));
            log.info("Mongo search body, httpId:{}, jmxId:{}, testCaseId:{}", httpVO.getId(), httpVO.getJmxId(), httpVO.getTestCaseId());
            JSONObject json = mongoTemplate.findOne(query, JSONObject.class, BODY_COLLECTION);
            log.info("Mongo search body, json:{}", JSON.toJSONString(json, true));
            if (ObjectUtils.isNotEmpty(json)) {
                httpVO.setBody(JSON.toJSONString(json.get("body"), true));
            }
            jmxVO.setHttpVO(httpVO);
        } else if (jmxDO.getJmeterSampleType().equals(JMeterSampleEnum.DUBBO_SAMPLE.getCode())) {
            jmxVO.setDubboVO(dubboService.getByJmxId(id));
        } else if (jmxDO.getJmeterSampleType().equals(JMeterSampleEnum.JAVA_REQUEST.getCode())) {
            JavaVO javaVO = javaService.getByJmxId(id);
            javaVO.setJavaParamVOList(javaParamService.getListByJavaId(javaVO.getId()));
            jmxVO.setJavaVO(javaVO);
        } else {
            throw new MysteriousException("sample类型异常, 请确认");
        }

        log.info("jmxVO: {}", JSON.toJSONString(jmxVO, true));
        return jmxVO;
    }

    @Synchronized
    @Transactional
    @Override
    public Boolean updateOnlineJmx(Long id, JmxVO jmxVO, UserVO userVO) {
        /** 直接更新脚本各个模块的表，以及对应XML的数据，主键ID必传 */
//        if (null == jmxVO.getId()) {
//            throw new MysteriousException("JMX主键为空");
//        }

        /** 如果jmx脚本关联了jar包和csv文件，先删除掉jar和csv，否则会导致脚本里配置的异常，因为上传依赖会修改脚本 */
        List<JarVO> jarVOList = jarService.getByTestCaseId(jmxVO.getTestCaseId());
        if (!CollectionUtils.isEmpty(jarVOList)) {
            throw new MysteriousException("该脚本关联了JAR文件,请先删除依赖,再更新在线脚本");
        }
        List<CsvVO> csvVOList = csvService.getByTestCaseId(jmxVO.getTestCaseId());
        if (!CollectionUtils.isEmpty(csvVOList)) {
            throw new MysteriousException("该脚本关联了CSV文件，请先删除依赖,再更新在线脚本");
        }

        //当前的脚本信息
        /**
         * 线程组可以改，因为更换压测模式
         * Sample无法修改，因为接口都变了？重新新增脚本
         */
        JmxDO dbJmxDO = jmxMapper.getById(id);
        if (null == dbJmxDO) {
            throw new MysteriousException("更新的JMX脚本不存在");
        }

        if (!dbJmxDO.getJmeterSampleType().equals(jmxVO.getJmeterSampleType())) {
            throw new MysteriousException("脚本Sample类型无法修改");
        }

        log.info("update, jmxVO: {}", JSON.toJSONString(jmxVO, true));

        /** 输出脚本 */
        String jmxFilePath = jmxVO.getJmxDir() + jmxVO.getDstName();
        log.info("jmxFilePath: {}", jmxFilePath);
        String debugJmxFilePath = jmxVO.getJmxDir() + "debug_" + jmxVO.getDstName();
        log.info("debugJmxFilePath: {}", debugJmxFilePath);

        checkJmxVO(jmxVO);
        String baseJmxFilePath = getBaseJmxFilePath(jmxVO);
        /** 每种类型，提供不同的base脚本 */
        jmeterXMLService.init(baseJmxFilePath);

        JmxDO jmxDO = BeanConverter.doSingle(jmxVO, JmxDO.class);
        /** 名称, 路径无法修改 */
        crudEntity.updateT(jmxDO, userVO);
        jmxMapper.update(jmxDO);


        /** 更新脚本要注意，如果更新为不同类型线程组的情况 */
        /** 线程组 */
        if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.THREAD_GROUP.getCode())) {
            ThreadGroupVO threadGroupVO = jmxVO.getThreadGroupVO();
            //ThreadGroup => ThreadGroup
            if (dbJmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.THREAD_GROUP.getCode())) {
                /** 入库 */
                threadGroupService.updateThreadGroup(threadGroupVO);
            } else if (dbJmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
                //SteppingThreadGroup => ThreadGroup
                //删除db里的stepping
                SteppingThreadGroupVO steppingThreadGroupVO = steppingThreadGroupService.getByJmxId(id);
                steppingThreadGroupService.deleteSteppingThreadGroup(steppingThreadGroupVO.getId());
                //新增一条threadgroup
                threadGroupVO.setJmxId(id);
                threadGroupVO.setTestCaseId(jmxVO.getTestCaseId());
                threadGroupService.addThreadGroup(threadGroupVO);
            } else if (dbJmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
                //ConcurrencyThreadGroup => ThreadGroup
                //删除db里的concurrency
                ConcurrencyThreadGroupVO concurrencyThreadGroupVO = concurrencyThreadGroupService.getByJmxId(id);
                concurrencyThreadGroupService.deleteConcurrencyThreadGroup(concurrencyThreadGroupVO.getId());
                //新增一条threadGroup
                threadGroupVO.setJmxId(jmxDO.getId());
                threadGroupVO.setTestCaseId(jmxDO.getTestCaseId());
                threadGroupService.addThreadGroup(threadGroupVO);
            } else {
                log.warn("jmeterThreadsType: {} => {}", dbJmxDO.getJmeterThreadsType(), jmxDO.getJmeterThreadsType());
            }
            //修改JMX脚本对应线程组参数
            log.info("threadGroupDO: {}", JSON.toJSONString(threadGroupVO, true));
            jmeterXMLService.updateThreadGroup(threadGroupVO);
        } else if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
            //更新为SteppingThreadGroup脚本
            SteppingThreadGroupVO steppingThreadGroupVO = jmxVO.getSteppingThreadGroupVO();
            if (dbJmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
                //Stepping => Stepping
                steppingThreadGroupService.updateSteppingThreadGroup(steppingThreadGroupVO);
            } else if (dbJmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.THREAD_GROUP.getCode())) {
                //ThreadGroup => Stepping
                //删除db里的threadgroup
                ThreadGroupVO threadGroupVO = threadGroupService.getByJmxId(id);
                threadGroupService.deleteThreadGroup(threadGroupVO.getId());
                //新增一条stepping
                steppingThreadGroupVO.setJmxId(jmxDO.getId());
                steppingThreadGroupVO.setTestCaseId(jmxDO.getTestCaseId());
                steppingThreadGroupService.addSteppingThreadGroup(steppingThreadGroupVO);
            } else if (dbJmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
                //Concurrency => Stepping
                ConcurrencyThreadGroupVO concurrencyThreadGroupVO = concurrencyThreadGroupService.getByJmxId(id);
                concurrencyThreadGroupService.deleteConcurrencyThreadGroup(concurrencyThreadGroupVO.getId());
                //新增一条Stepping
                steppingThreadGroupVO.setJmxId(jmxDO.getId());
                steppingThreadGroupVO.setTestCaseId(jmxDO.getTestCaseId());
                steppingThreadGroupService.addSteppingThreadGroup(steppingThreadGroupVO);
            } else {
                log.warn("jmeterThreadsType: {} => {}", dbJmxDO.getJmeterThreadsType(), jmxDO.getJmeterThreadsType());
            }
            //修改JMX脚本对应梯度加压参数
            log.info("steppingThreadGroupDO: {}", JSON.toJSONString(steppingThreadGroupVO, true));
            jmeterXMLService.updateSteppingThreadGroup(steppingThreadGroupVO);
        } else if (jmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
            //更新为ConcurrencyThreadGroup脚本
            ConcurrencyThreadGroupVO concurrencyThreadGroupVO = jmxVO.getConcurrencyThreadGroupVO();
            if (dbJmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
                //Concurrency => Concurrency
                concurrencyThreadGroupService.updateConcurrencyThreadGroup(concurrencyThreadGroupVO);
            } else if (dbJmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.THREAD_GROUP.getCode())) {
                //ThreadGroup => Concurrency
                ThreadGroupVO threadGroupVO = threadGroupService.getByJmxId(id);
                threadGroupService.deleteThreadGroup(threadGroupVO.getId());
                //add
                concurrencyThreadGroupVO.setJmxId(jmxDO.getId());
                concurrencyThreadGroupVO.setTestCaseId(jmxDO.getTestCaseId());
                concurrencyThreadGroupService.addConcurrencyThreadGroup(concurrencyThreadGroupVO);
            } else if (dbJmxDO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
                //Stepping => Concurrency
                SteppingThreadGroupVO steppingThreadGroupVO = steppingThreadGroupService.getByJmxId(id);
                steppingThreadGroupService.deleteSteppingThreadGroup(steppingThreadGroupVO.getId());
                //add
                concurrencyThreadGroupVO.setJmxId(jmxDO.getId());
                concurrencyThreadGroupVO.setTestCaseId(jmxDO.getTestCaseId());
                concurrencyThreadGroupService.addConcurrencyThreadGroup(concurrencyThreadGroupVO);
            } else {
                log.warn("jmeterThreadsType: {} => {}", dbJmxDO.getJmeterThreadsType(), jmxDO.getJmeterThreadsType());
            }
            //修改JMX脚本对应加压参数
            log.info("concurrencyThreadGroupVO: {}", JSON.toJSONString(concurrencyThreadGroupVO, true));
            jmeterXMLService.updateConcurrencyThreadGroup(concurrencyThreadGroupVO);
        } else {
            throw new MysteriousException("线程组类型异常, 请确认");
        }

        /** Sample
         * 更新脚本的时候，Sample不能修改，也就是比如当前是个HTTP请求，不能修改成Java Request */
        if (jmxDO.getJmeterSampleType().equals(JMeterSampleEnum.HTTP_REQUEST.getCode())) {
            //http
            HttpVO httpVO = jmxVO.getHttpVO();
            httpService.updateHttp(httpVO);
            //修改JMX脚本里HTTP信息
            jmeterXMLService.updateHttpSample(httpVO);

            /**  mongodb更新body */
            String body = httpVO.getBody();
            if (!checkHttpBodyIsEmpty(body)) {
                Query query = new Query(Criteria
                        .where("httpId").is(httpVO.getId())
                        .and("jmxId").is(httpVO.getJmxId())
                        .and("testCaseId").is(httpVO.getTestCaseId()));
                Update update = new Update();
                update.set("body", JSON.parseObject(body));
                log.info("Mongo update body, httpId:{}, jmxId:{}, testCaseId:{}", httpVO.getId(), httpVO.getJmxId(), httpVO.getTestCaseId());
                log.info("Mongo update body, body:{}", JSON.parseObject(body).toJSONString());
                mongoTemplate.updateFirst(query, update, BODY_COLLECTION);
                /** add body会jmx里先清理body节点，再新增 */
                jmeterXMLService.addHttpBody(httpVO.getBody());
            }

            /** Http header和param的更新（太麻烦，放弃） */
            /** 1、更新的有，表里没有的，入库
             *  2、更新的没有，表里有的，删除
             *  3、更新的有，表里也有的，更新 */

            /* 这种分别求差集，交集再入库的方法太麻烦了，放弃
            List<HttpHeaderDO> newHeaderDOList = httpDO.getHttpHeaderDOList();
            List<HttpHeaderDO> dbHeaderDOList = httpHeaderService.getListByHttpId(httpDO.getId());

            List<HttpHeaderDO> addHeaderDOList = new ArrayList<>(newHeaderDOList);
            List<HttpHeaderDO> deleteHeaderDOList = new ArrayList<>(dbHeaderDOList);
            List<HttpHeaderDO> updateHeaderDOList = addHeaderDOList;

            addHeaderDOList.removeAll(dbHeaderDOList);
            httpHeaderService.batchUpdateHttpHeader(addHeaderDOList);

            deleteHeaderDOList.removeAll(newHeaderDOList);
            httpHeaderService.batchDeleteHttpHeader(deleteHeaderDOList.stream().map(HttpHeaderDO::getId).collect(Collectors.toList()));

            updateHeaderDOList.retainAll(dbHeaderDOList);
            httpHeaderService.batchUpdateHttpHeader(updateHeaderDOList);
            */

            /** Http header和param的更新，直接删掉表里的，然后将请求里的入库 */
            List<HttpHeaderVO> dbHeaderVOList = httpHeaderService.getListByHttpId(httpVO.getId());
            if (!CollectionUtils.isEmpty(dbHeaderVOList)) {
                log.info("HTTP删除dbHeaderVOList: {}", dbHeaderVOList);
                httpHeaderService.batchDeleteHttpHeader(
                        dbHeaderVOList.stream().map(HttpHeaderVO::getId).collect(Collectors.toList()));
                jmeterXMLService.cleanHttpHeader();
            }
            /** 传过来的header */
            List<HttpHeaderVO> headerVOList = httpVO.getHttpHeaderVOList();
            log.info("HTTP新增headerVOList: {}", headerVOList);
            if (!CollectionUtils.isEmpty(headerVOList)) {
                for (HttpHeaderVO httpHeaderVO : headerVOList) {
                    if (!CollectionUtils.isEmpty(httpHeaderService.getExistHeaderList(httpVO.getId(), httpHeaderVO.getHeaderKey()))) {
                        throw new MysteriousException("HTTP Header: " + httpHeaderVO.getHeaderKey() + " 已存在");
                    }
                    httpHeaderVO.setTestCaseId(jmxDO.getTestCaseId());
                    httpHeaderVO.setJmxId(id);
                    httpHeaderVO.setHttpId(httpVO.getId());
                    httpHeaderService.addHttpHeader(httpHeaderVO);
                    jmeterXMLService.addHttpHeader(httpHeaderVO.getHeaderKey(), httpHeaderVO.getHeaderValue());
                }
            }

            List<HttpParamVO> dbParamVOList = httpParamService.getListByHttpId(httpVO.getId());
            if (!CollectionUtils.isEmpty(dbParamVOList)) {
                log.info("HTTP删除dbParamVOList: {}", dbParamVOList);
                httpParamService.batchDeleteHttpParam(
                        dbParamVOList.stream().map(HttpParamVO::getId).collect(Collectors.toList()));
                jmeterXMLService.cleanHttpParam();
            }
            /** 传过来的param */
            List<HttpParamVO> paramVOList = httpVO.getHttpParamVOList();
            log.info("HTTP新增paramVOList: {}", paramVOList);
            if (!CollectionUtils.isEmpty(paramVOList)) {
                for (HttpParamVO httpParamVO : paramVOList) {
                    if (!CollectionUtils.isEmpty(httpParamService.getExistParamList(httpVO.getId(), httpParamVO.getParamKey()))) {
                        throw new MysteriousException("HTTP Param: " + httpParamVO.getParamKey() + " 已存在");
                    }
                    httpParamVO.setTestCaseId(jmxVO.getTestCaseId());
                    httpParamVO.setJmxId(id);
                    httpParamVO.setHttpId(httpVO.getId());
                    httpParamService.addHttpParam(httpParamVO);
                    jmeterXMLService.addHttpParam(httpParamVO.getParamKey(), httpParamVO.getParamValue());
                }
            }
        } else if (jmxVO.getJmeterSampleType().equals(JMeterSampleEnum.JAVA_REQUEST.getCode())) {
            //java request
            JavaVO javaVO = jmxVO.getJavaVO();
            javaService.updateJava(javaVO);
            jmeterXMLService.updateJavaRequest(javaVO.getJavaRequestClassPath());

            /** 删除已有的param */
            List<JavaParamVO> dbParamVOList = javaParamService.getListByJavaId(javaVO.getId());
            if (!CollectionUtils.isEmpty(dbParamVOList)) {
                log.info("Java删除dbParamVOList: {}", dbParamVOList);
                javaParamService.batchDeleteJavaParam(
                        dbParamVOList.stream().map(JavaParamVO::getId).collect(Collectors.toList()));
                jmeterXMLService.cleanJavaParam();
            }

            /** 新增传入的param */
            List<JavaParamVO> paramVOList = javaVO.getJavaParamVOList();
            log.info("Java新增paramVOList: {}", paramVOList);
            if (!CollectionUtils.isEmpty(paramVOList)) {
                for (JavaParamVO javaParamVO : paramVOList) {
                    if (!CollectionUtils.isEmpty(javaParamService.getExistParamList(javaVO.getId(), javaParamVO.getParamKey()))) {
                        throw new MysteriousException("Java Param: " + javaParamVO.getParamKey() + " 已存在");
                    }
                    javaParamVO.setTestCaseId(jmxVO.getTestCaseId());
                    javaParamVO.setJmxId(id);
                    javaParamVO.setJavaId(javaVO.getId());
                    javaParamService.addJavaParam(javaParamVO);
                    jmeterXMLService.addJavaParam(javaParamVO.getParamKey(), javaParamVO.getParamValue());
                }
            }
        } else if (jmxDO.getJmeterSampleType().equals(JMeterSampleEnum.DUBBO_SAMPLE.getCode())) {
            //dubbo sample

        } else {

        }


        /** 将JMX的更改写入指定路径脚本 */
        jmeterXMLService.writeJmxFile(jmxFilePath);

        /** 将jmx脚本拷贝一份，修改线程变量，调试执行一次使用 */
        fileUtils.copyFile(jmxFilePath, debugJmxFilePath);

        /** 拷贝完之后，修改debug脚本的线程数，全部修改为1，使得只执行一次 */
        //JMeterUtil jmeter = new JMeterUtil();
        jMeterUtil.updateDebugThread(debugJmxFilePath);

        return true;
    }

    @Override
    public Boolean forceDelete(Long id) {
        //jmx
        JmxDO jmxDO = jmxMapper.getById(id);
        if (null != jmxDO) {
            jmxMapper.delete(id);
        }
        //threadgroup
        ThreadGroupVO threadGroupVO = threadGroupService.getByJmxId(id);
        if (null != threadGroupVO) {
            threadGroupService.deleteThreadGroup(threadGroupVO.getId());
        }

        //stepping
        SteppingThreadGroupVO steppingThreadGroupVO = steppingThreadGroupService.getByJmxId(id);
        if (null != steppingThreadGroupVO) {
            steppingThreadGroupService.deleteSteppingThreadGroup(steppingThreadGroupVO.getId());
        }

        //concurrency
        ConcurrencyThreadGroupVO concurrencyThreadGroupVO = concurrencyThreadGroupService.getByJmxId(id);
        if (null != concurrencyThreadGroupVO) {
            concurrencyThreadGroupService.deleteConcurrencyThreadGroup(concurrencyThreadGroupVO.getId());
        }

        //httpDO
        HttpVO httpVO = httpService.getByJmxId(id);
        if (null != httpVO) {
            httpService.deleteHttp(httpVO.getId());
        }

        //body
        //String body = httpDO.getBody();
        //if (StringUtils.isNotBlank(body)) {
        Query query = new Query(Criteria
                .where("httpId").is(httpVO.getId())
                .and("jmxId").is(httpVO.getJmxId())
                .and("testCaseId").is(httpVO.getTestCaseId()));
        mongoTemplate.remove(query, BODY_COLLECTION);
        //}

        //header
        List<HttpHeaderVO> httpHeaderVOList = httpHeaderService.getListByJmxId(id);
        if (!CollectionUtils.isEmpty(httpHeaderVOList)) {
            httpHeaderVOList.forEach(httpHeaderVO -> httpHeaderService.deleteHttpHeader(httpHeaderVO.getId()));
        }

        //param
        List<HttpParamVO> httpParamVOList = httpParamService.getListByJmxId(id);
        if (!CollectionUtils.isEmpty(httpParamVOList)) {
            httpParamVOList.forEach(httpParamVO -> httpParamService.deleteHttpParam(httpParamVO.getId()));
        }

        //javaDO
        JavaVO javaVO = javaService.getByJmxId(id);
        if (null != javaVO) {
            javaService.deleteJava(javaVO.getId());
        }

        //java param
        List<JavaParamVO> javaParamVOList = javaParamService.getListByJmxId(id);
        if (!CollectionUtils.isEmpty(javaParamVOList)) {
            javaParamVOList.forEach(javaParamVO -> javaParamService.deleteJavaParam(javaParamVO.getId()));
        }
        return true;
    }
}
