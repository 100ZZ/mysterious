package com.lihuia.mysterious.service.service.jmx.impl;

import com.alibaba.fastjson.JSON;
import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.io.MysteriousFileUtils;
import com.lihuia.mysterious.common.jmeter.JMeterUtil;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import com.lihuia.mysterious.core.mapper.jmx.JmxMapper;
import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.jmx.JmxQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.report.ReportVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseFullVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.enums.JMeterScriptEnum;
import com.lihuia.mysterious.service.service.config.IConfigService;
import com.lihuia.mysterious.service.service.csv.impl.CsvService;
import com.lihuia.mysterious.service.service.jar.impl.JarService;
import com.lihuia.mysterious.service.service.jmx.IJmxService;
import com.lihuia.mysterious.service.service.testcase.ITestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
    private CRUDEntity<JmxDO> crudEntity;

    @Autowired
    private MysteriousFileUtils fileUtils;

    @Autowired
    private JMeterUtil jMeterUtil;

    @Autowired
    private JarService jarService;

    @Autowired
    private CsvService csvService;

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
        TestCaseFullVO testCaseFullVO = testCaseService.getFull(testCaseId);
        if (!ObjectUtils.isEmpty(testCaseFullVO.getJmxVO())) {
            throw new MysteriousException(ResponseCodeEnum.TESTCASE_HAS_JMX);
        }

        /** 上传的jmx文件名，带后缀*/
        String srcName = jmxFile.getOriginalFilename();
        if (StringUtils.isEmpty(srcName) || srcName.contains(" ")) {
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
        /** 2021-7-31 改脚本是上传的 */
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
    public JmxVO getById(Long id) {
        JmxDO jmxDO = jmxMapper.getById(id);
        if (ObjectUtils.isEmpty(jmxDO)) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        JmxVO jmxVO = BeanConverter.doSingle(jmxDO, JmxVO.class);
        jmxVO.setId(jmxDO.getId());
        return jmxVO;
    }

    @Override
    public PageVO<JmxVO> getJmxList(JmxQuery jmxQuery) {
        PageVO<JmxVO> pageVO = new PageVO<>();
        Integer page = jmxQuery.getPage();
        Integer size = jmxQuery.getSize();
        String srcName = jmxQuery.getSrcName();
        Long testCaseId = jmxQuery.getTestCaseId();
        Long creatorId = jmxQuery.getCreatorId();
        Integer offset = pageVO.getOffset(page, size);
        Integer total = jmxMapper.getJmxCount(srcName, testCaseId, creatorId);
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<JmxDO> jmxDOList = jmxMapper.getJmxList(srcName, testCaseId, creatorId, offset, size);
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
    public Boolean runJmx(CommandLine commandLine, TestCaseVO testCaseVO, ReportVO reportVO, UserVO userVO) {
        return null;
    }

    @Override
    public Boolean debugJmx(CommandLine commandLine, TestCaseVO testCaseVO, ReportVO reportVO, UserVO userVO) {
        return null;
    }

    @Override
    public Boolean stopJmx(CommandLine commandLine, TestCaseVO testCaseVO, UserVO userVO) {
        return null;
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
