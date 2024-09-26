package com.lihuia.mysterious.service.service.jar.impl;

import com.alibaba.fastjson.JSON;
import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.io.MysteriousFileUtils;
import com.lihuia.mysterious.common.jmeter.JMeterUtil;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.common.ssh.SSHUtils;
import com.lihuia.mysterious.core.entity.jar.JarDO;
import com.lihuia.mysterious.core.mapper.jar.JarMapper;
import com.lihuia.mysterious.core.vo.jar.JarQuery;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.testcase.TestCaseFullVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.service.config.IConfigService;
import com.lihuia.mysterious.service.service.jar.IJarService;
import com.lihuia.mysterious.service.service.node.INodeService;
import com.lihuia.mysterious.service.service.testcase.ITestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午4:36
 */

@Slf4j
@Service
public class JarService implements IJarService {

    @Autowired
    private ITestCaseService testCaseService;

    @Autowired
    private JarMapper jarMapper;

    @Autowired
    private IConfigService configService;

    @Autowired
    private INodeService nodeService;

    @Autowired
    private MysteriousFileUtils fileUtils;

    @Autowired
    private CRUDEntity<JarDO> crudEntity;

    @Autowired
    private JMeterUtil jMeterUtil;

    private Boolean isJmeterPluginJar(String jarFile) {
        String regex = "^jmeter-plugins-.*\\.jar";
        Matcher matcher = Pattern.compile(regex).matcher(jarFile);
        return matcher.find();
    }

    private void checkJarParam(JarVO jarVO) {
        if (ObjectUtils.isEmpty(jarVO)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        if (ObjectUtils.isEmpty(jarVO.getId())) {
            throw new MysteriousException(ResponseCodeEnum.PARAM_MISSING);
        }
        if (ObjectUtils.isEmpty(jarMapper.getById(jarVO.getId()))) {
            throw new MysteriousException(ResponseCodeEnum.JAR_NOT_EXIST);
        }
    }

    @Transactional
    @Override
    public Boolean uploadJar(Long testCaseId, MultipartFile jarFile, UserVO userVO) {
        TestCaseFullVO testCaseFullVO = testCaseService.getFullVO(testCaseId);
        /** jar上传会修改jmx脚本里jar包classpath绝对路径，因此依赖jmx脚本存在 */
        if (ObjectUtils.isEmpty(testCaseFullVO.getJmxVO())) {
            throw new MysteriousException(ResponseCodeEnum.JMX_NOT_EXIST);
        }
        /** 用例jar包完整路径 */
        String jarFileName = jarFile.getOriginalFilename();
        if (ObjectUtils.isEmpty(jarFileName)
                || !jarFileName.contains(".jar")
                || StringUtils.isBlank(jarFileName)
                || jarFileName.contains(" ")) {
            throw new MysteriousException(ResponseCodeEnum.JAR_NAME_ERROR);
        }
        /** 如果是以jmeter-plugins-开头的jar包，当做是插件依赖包，放在系统/lib/ext下;
         *  其他普通的jar包还是放在用例文件夹管理的/jar/目录下 */
        String jarDir;
        if (isJmeterPluginJar(jarFileName)) {
            jarDir = configService.getValue(JMeterUtil.MASTER_JMETER_HOME) + "/lib/ext/";
        } else {
            jarDir = testCaseFullVO.getTestCaseDir() + "jar/";
        }

        /** 上传前需要校验，这个jar包是否已经存在，根据name+path决定 */
        if (!CollectionUtils.isEmpty(jarMapper.getExistJarList(testCaseId, jarFileName, jarDir))) {
            throw new MysteriousException(ResponseCodeEnum.JAR_IS_EXIST);
        }
        /** 更新jar表 */
        JarDO jarDO = new JarDO();
        jarDO.setSrcName(jarFileName);
        jarDO.setDstName(jarFileName);
        jarDO.setDescription(testCaseFullVO.getName());
        jarDO.setTestCaseId(testCaseId);
        jarDO.setJarDir(jarDir);
        log.info("新增JAR: {}", JSON.toJSONString(jarDO));
        crudEntity.addT(jarDO, userVO);
        jarMapper.add(jarDO);

        /** 上传jar包到master节点 */
        String jarFilePath = jarDir + jarFileName;
        log.info("上传JAR文件到master节点, jarFilePath: {}, jarFile: {}", jarFilePath, jarFile.toString());
        fileUtils.uploadFile(jarFilePath, jarFile);
        /** 同步jar包到slave节点 */
        List<NodeVO> enableNodeList = nodeService.getEnableNodeList();
        log.info("上传JAR文件， 已启用的slave节点: {}", JSON.toJSONString(enableNodeList));
        if (!CollectionUtils.isEmpty(enableNodeList)) {
            for (NodeVO nodeVO : enableNodeList) {
                /** 将jar包同步到每个slave节点，路径和master一致 */
                SSHUtils ssh = new SSHUtils(nodeVO.getHost(), nodeVO.getPort(), nodeVO.getUsername(), nodeVO.getPassword());
                ssh.scpFile(jarFilePath, jarDir);
            }
        }

        JmxVO jmxVO = testCaseFullVO.getJmxVO();
        /** 定位到Master节点的jmx脚本，这个是重命名后的脚本全路径 */
        String jmxFilePath = jmxVO.getJmxDir() + jmxVO.getDstName();
        String debugJmxFilePath = jmxVO.getJmxDir() + "debug_" + jmxVO.getDstName();
        /** 修改Master节点上，JMX脚本文件里JAR的文件路径 */

        /**
         * jmxFilePath：master节点要修改的脚本文件
         * jarFilePath：jmx脚本里jar的路径
         */
        log.info("上传JAR文件后, 更新JMX脚本, jmxFilePath: {}, jarFilePath: {}", jmxFilePath, testCaseFullVO.getTestCaseDir() + "jar");
        jMeterUtil.updateJmxJarFilePath(jmxFilePath, testCaseFullVO.getTestCaseDir() + "jar");
        jMeterUtil.updateJmxJarFilePath(debugJmxFilePath, testCaseFullVO.getTestCaseDir() + "jar");

        return true;
    }

    @Override
    public Boolean updateJar(JarVO jarVO, UserVO userVO) {
        checkJarParam(jarVO);
        JarDO jarDO = BeanConverter.doSingle(jarVO, JarDO.class);
        jarDO.setId(jarVO.getId());
        crudEntity.updateT(jarDO, userVO);
        return jarMapper.update(jarDO) > 0;
    }

    @Transactional
    @Override
    public Boolean deleteJar(Long id) {
        JarDO jarDO = jarMapper.getById(id);
        if (ObjectUtils.isEmpty(jarDO)) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        log.info("删除JAR: {}", id);
        jarMapper.delete(id);

        /** 如果是jmeter-plugins-开头的包，上传回到lib/ext目录下，就没必要再删除文件了，删除表记录就行了 */
        if (isJmeterPluginJar(jarDO.getSrcName())) {
            log.info("没必要删除系统路径包: {}", jarDO.getSrcName());
            return true;
        }
        /** Jar脚本文件保存在master和slave节点上，先删除文件, 再删除记录 */
        /** nps部署的机器master节点，删除 */
        log.info("删除master节点JAR文件: {}", JSON.toJSONString(jarDO));
        fileUtils.rmFile(jarDO.getJarDir() + jarDO.getDstName());
        /** slave节点机器删除 */
        List<NodeVO> enableNodeList = nodeService.getEnableNodeList();
        log.info("删除JAR文件， 已启用的slave节点: {}", JSON.toJSONString(enableNodeList));
        if (!CollectionUtils.isEmpty(enableNodeList)) {
            for (NodeVO nodeVO : enableNodeList) {
                SSHUtils ssh = new SSHUtils(nodeVO.getHost(), nodeVO.getPort(), nodeVO.getUsername(), nodeVO.getPassword());
                ssh.execCommand("rm -rf " + jarDO.getJarDir() + jarDO.getDstName());
            }
        }
        return true;
    }

    @Override
    public PageVO<JarVO> getJarList(JarQuery query) {
        PageVO<JarVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(query.getPage(), query.getSize());
        Integer total = jarMapper.getJarCount(query.getSrcName(), query.getTestCaseId());
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<JarDO> jarDOList =
                    jarMapper.getJarList(query.getSrcName(), query.getTestCaseId(), offset, query.getSize());
            if (!CollectionUtils.isEmpty(jarDOList)) {
                pageVO.setList(jarDOList.stream().map(jarDO -> {
                    JarVO jarVO = BeanConverter.doSingle(jarDO, JarVO.class);
                    jarVO.setId(jarDO.getId());
                    return jarVO;
                }).collect(Collectors.toList()));
            }
        }
        return pageVO;
    }

    @Override
    public List<JarVO> getByTestCaseId(Long testCaseId) {
        List<JarDO> jarDOList = jarMapper.getByTestCaseId(testCaseId);
        if (CollectionUtils.isEmpty(jarDOList)) {
            return Collections.emptyList();
        }
        return jarDOList.stream().map(jarDO -> {
            JarVO jarVO = BeanConverter.doSingle(jarDO, JarVO.class);
            jarVO.setId(jarDO.getId());
            return jarVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<JarDO> getJarDOList(Long testCaseId) {
        return jarMapper.getByTestCaseId(testCaseId);
    }

    @Override
    public JarVO getJarVO(Long id) {
        JarDO jarDO = jarMapper.getById(id);
        if (ObjectUtils.isEmpty(jarDO)) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        JarVO jarVO = BeanConverter.doSingle(jarDO, JarVO.class);
        jarVO.setId(id);
        return jarVO;
    }
}
