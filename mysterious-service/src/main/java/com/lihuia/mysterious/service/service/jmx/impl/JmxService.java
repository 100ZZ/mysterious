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
import com.lihuia.mysterious.core.entity.jmx.sample.java.JavaDO;
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
 * @author maple@lihuia.com
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

    private void checkJmxDO(JmxVO jmxVO) {

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
        checkJmxDO(jmxVO);
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
            ThreadGroupDO threadGroupDO = BeanConverter.doSingle(threadGroupVO, ThreadGroupDO.class);
            threadGroupDO.setJmxId(jmxId);
            threadGroupDO.setTestCaseId(jmxDO.getTestCaseId());
            /** 默认线程组入库 */
            threadGroupService.addThreadGroup(threadGroupDO);
            /** 线程组扩充 */
            jmeterXMLService.updateThreadGroup(threadGroupDO);

        } else if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.STEPPING_THREAD_GROUP.getCode())) {
            SteppingThreadGroupVO steppingThreadGroupVO = jmxVO.getSteppingThreadGroupVO();
            SteppingThreadGroupDO steppingThreadGroupDO = BeanConverter.doSingle(steppingThreadGroupVO, SteppingThreadGroupDO.class);
            steppingThreadGroupDO.setTestCaseId(testCaseId);
            steppingThreadGroupDO.setJmxId(jmxId);
            /** 梯度线程组入库 */
            steppingThreadGroupService.addSteppingThreadGroup(steppingThreadGroupDO);
            /** todo 梯度线程组扩充 */
            jmeterXMLService.updateSteppingThreadGroup(steppingThreadGroupDO);
        } else if (jmxVO.getJmeterThreadsType().equals(JMeterThreadsEnum.CONCURRENCY_THREAD_GROUP.getCode())) {
            ConcurrencyThreadGroupVO concurrencyThreadGroupVO = jmxVO.getConcurrencyThreadGroupVO();
            ConcurrencyThreadGroupDO concurrencyThreadGroupDO = BeanConverter.doSingle(concurrencyThreadGroupVO, ConcurrencyThreadGroupDO.class);
            concurrencyThreadGroupDO.setTestCaseId(testCaseId);
            concurrencyThreadGroupDO.setJmxId(jmxId);
            /** 线程组入库 */
            concurrencyThreadGroupService.addConcurrencyThreadGroup(concurrencyThreadGroupDO);
            jmeterXMLService.updateConcurrencyThreadGroup(concurrencyThreadGroupDO);
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
            String body = newHttpVO.getBody();
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
        return null;
    }

    @Override
    public Boolean updateOnlineJmx(JmxVO jmxVO, UserVO userVO) {
        return null;
    }

    @Override
    public Boolean forceDelete(Long id) {
        return null;
    }
}
