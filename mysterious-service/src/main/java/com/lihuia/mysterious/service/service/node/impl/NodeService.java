package com.lihuia.mysterious.service.service.node.impl;

import com.alibaba.fastjson.JSON;
import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.jmeter.JMeterUtil;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.common.ssh.SSHUtils;
import com.lihuia.mysterious.core.entity.node.NodeDO;
import com.lihuia.mysterious.core.mapper.node.NodeMapper;
import com.lihuia.mysterious.core.vo.node.NodeParam;
import com.lihuia.mysterious.core.vo.node.NodeQuery;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.enums.NodeStatusEnum;
import com.lihuia.mysterious.service.enums.NodeTypeEnum;
import com.lihuia.mysterious.service.service.config.IConfigService;
import com.lihuia.mysterious.service.service.node.INodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author maple@lihuia.com
 * @date 2023/3/27 10:55 PM
 */

@Slf4j
@Service
public class NodeService implements INodeService {

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private IConfigService configService;

    @Autowired
    private CRUDEntity<NodeDO> crudEntity;

    private void checkNodeParam(NodeParam nodeParam) {
        if (ObjectUtils.isEmpty(nodeParam)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        if (StringUtils.isEmpty(nodeParam.getHost())
                || StringUtils.isEmpty(nodeParam.getUsername())
                || ObjectUtils.isEmpty(nodeParam.getPort())
                || ObjectUtils.isEmpty(nodeParam.getType()) ) {
            throw new MysteriousException(ResponseCodeEnum.PARAM_MISSING);
        }
    }

    private void checkNodeExist(NodeParam nodeParam) {
        List<NodeDO> nodeList = nodeMapper.getByHost(nodeParam.getHost());
        if (!CollectionUtils.isEmpty(nodeList)) {
            throw new MysteriousException(ResponseCodeEnum.NODE_EXIST);
        }
    }

    @Override
    public Long addNode(NodeParam nodeParam, UserVO userVO) {
        checkNodeParam(nodeParam);
        checkNodeExist(nodeParam);
        NodeDO nodeDO = BeanConverter.doSingle(nodeParam, NodeDO.class);
        nodeDO.setStatus(NodeStatusEnum.DISABLED.getCode());
        crudEntity.addT(nodeDO, userVO);
        nodeMapper.add(nodeDO);
        return nodeDO.getId();
    }

    @Override
    public Boolean updateNode(Long id, NodeParam nodeParam, UserVO userVO) {
        NodeDO nodeDO = nodeMapper.getById(id);
        if (ObjectUtils.isEmpty(nodeDO)) {
            return false;
        }
        log.info("nodeDO:{}", nodeDO);
        nodeDO = BeanConverter.doSingle(nodeParam, NodeDO.class);
        nodeDO.setId(id);
        crudEntity.updateT(nodeDO, userVO);
        return nodeMapper.update(nodeDO) > 0;
    }

    @Override
    public List<NodeVO> getEnableNodeList() {
        List<NodeDO> nodeDOList =
                nodeMapper.getEnableNodeList(NodeTypeEnum.SLAVE.getCode(), NodeStatusEnum.ENABLE.getCode());
        if (CollectionUtils.isEmpty(nodeDOList)) {
            return Collections.emptyList();
        }
        return nodeDOList.stream().map(nodeDO -> {
            NodeVO nodeVO = BeanConverter.doSingle(nodeDO, NodeVO.class);
            nodeVO.setId(nodeDO.getId());
            return nodeVO;
        }).collect(Collectors.toList());
    }

    @Override
    public NodeVO getById(Long id) {
        NodeDO nodeDO = nodeMapper.getById(id);
        if (ObjectUtils.isEmpty(nodeDO)) {
            return null;
        }
        NodeVO nodeVO = BeanConverter.doSingle(nodeDO, NodeVO.class);
        nodeVO.setId(id);
        nodeVO.setPassword("******");
        return nodeVO;
    }

    @Override
    public Boolean deleteNode(Long id) {
        NodeDO nodeDO = nodeMapper.getById(id);
        if (ObjectUtils.isEmpty(nodeDO)) {
            return false;
        }
        return nodeMapper.delete(id) > 0;
    }

    @Override
    public PageVO<NodeVO> getNodeList(NodeQuery query) {
        PageVO<NodeVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(query.getPage(), query.getSize());
        Integer total = nodeMapper.getNodeCount(query.getName(), query.getHost());
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<NodeDO> nodeDOList = nodeMapper.getNodeList(query.getName(), query.getHost(), offset, query.getSize());
            pageVO.setList(nodeDOList.stream().map(nodeDO -> {
                NodeVO nodeVO = BeanConverter.doSingle(nodeDO, NodeVO.class);
                nodeVO.setId(nodeDO.getId());
                nodeVO.setPassword("******");
                return nodeVO;
            }).collect(Collectors.toList()));
        }
        return pageVO;
    }

    /**
     * 使用正则表达式校验MD5合法性
     */
    public boolean checkMD5(String md5Str) {
        return Pattern.matches("^([a-fA-F0-9]{32})$", md5Str);
    }

    @Transactional
    public void enableSlaveNode(NodeDO nodeDO, UserVO userVO) {
        nodeDO.setStatus(NodeStatusEnum.ENABLE.getCode());
        crudEntity.updateT(nodeDO, userVO);

        log.info("启用, 更新节点: {}", JSON.toJSONString(nodeDO, true));
        nodeMapper.update(nodeDO);
        /** slave节点jmeter-server的路径 */
        String slaveJmeterBinHome = configService.getValue(JMeterUtil.SLAVE_JMETER_BIN_HOME);
        String slaveJmeterServer = slaveJmeterBinHome + File.separator + "jmeter-server";
        /** slave节点jmeter-server执行日志路径 */
        String slaveJmeterLogHome = configService.getValue(JMeterUtil.SLAVE_JMETER_LOG_HOME);

        /** slave节点指定host启动jmeter-server */
        String host = nodeDO.getHost();
        Integer port = nodeDO.getPort();
        String username = nodeDO.getUsername();
        String password = nodeDO.getPassword();
        SSHUtils ssh = new SSHUtils(host, port, username, password);

        log.info("检查节点连通性");
        /** 检查连通性 */
        Boolean isConnected = false;
        try {
            isConnected = ssh.telnet(200);
        } catch (Exception e) {
            log.info("节点无法连通, {}", e.toString());
            throw new MysteriousException(ResponseCodeEnum.NODE_CANNOT_CONNECT);
        }
        if (!isConnected) {
            log.info("节点无法连通, isConnected: false");
            throw new MysteriousException(ResponseCodeEnum.NODE_CANNOT_CONNECT);
        }
        /** 检查文件 */
        log.info("检查md5");
        String md5str = null;
        try {
            md5str = ssh.execCommand("md5sum " + slaveJmeterServer + " | cut -d ' ' -f 1");
        } catch (Exception e) {
            log.info("检查md5命令异常, 请确认SSH连接以及登录验证是否正确");
            throw new MysteriousException(ResponseCodeEnum.SSH_AND_LOGIN_ERROR);
        }
        if (!checkMD5(md5str)) {
            log.info("节点: {}, 找不到jmeter-server文件", nodeDO.getName());
            throw new MysteriousException(ResponseCodeEnum.JMETER_SERVER_NOT_FOUND);
        }

        /** 检查jmeter-server进程 */
        log.info("检查jmeter-server进程");
        if(!"null".equals(ssh.execCommand("ps aux | grep jmeter-server | grep -v grep"))) {
            throw new MysteriousException(ResponseCodeEnum.JMETER_SERVER_IS_ENABLE);
        }
        /** 启动jmeter-server进程 */
        log.info("启动jmeter-server进程");
        String result = ssh.execCommand("cd " + slaveJmeterLogHome + "\n" +
                slaveJmeterServer + " -Djava.rmi.server.hostname=" + host);
        /** 这里返回的结果，华测开发环境和腾讯云主机返回结果不同, 因此更改下判断条件 */
        /** CentOS版本一: 返回结果为: Using local port: 1099 */
        /** CentOS版本二: 返回结果为: Created remote object: UnicastServerRef2 [liveRef: [endpoint:[172.30.64.14:11887](local),objID:[-399d775e:1772e15c6ba:-7fff, -1828385573382256740]]] */
        log.info("启动命令行输出: {}", result);
        if (!result.contains(host) && !result.contains("Using local port")) {
            throw new MysteriousException(ResponseCodeEnum.JMETER_SERVER_ENABLE_ERROR);
        }

        /** 启动后，检查进程 */
        log.info("启动后, 再次检查jmeter-server进程");
        if ("null".equals(ssh.execCommand("ps aux | grep jmeter-server | grep -v grep"))) {
            throw new MysteriousException(ResponseCodeEnum.JMETER_SERVER_IS_NOT_ENABLE);
        }
    }

    @Override
    public Boolean enableNode(Long id, UserVO userVO) {
        NodeDO nodeDO = nodeMapper.getById(id);
        if (ObjectUtils.isEmpty(nodeDO) || !NodeTypeEnum.SLAVE.getCode().equals(nodeDO.getType())) {
            throw new MysteriousException(ResponseCodeEnum.NODE_TYPE_ERROR);
        }
        if (NodeStatusEnum.ENABLE.getCode().equals(nodeDO.getStatus())) {
            throw new MysteriousException(ResponseCodeEnum.NODE_IS_ENABLE);
        }
        try {
            log.info("start enableSlaveNode");
            enableSlaveNode(nodeDO, userVO);
        } catch (Exception e) {
            nodeDO.setStatus(NodeStatusEnum.FAILED.getCode());
            crudEntity.updateT(nodeDO, userVO);
            log.info("启动节点失败, id: {}", nodeDO.getId());
            nodeMapper.update(nodeDO);
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean disableNode(Long id, UserVO userVO) {
        NodeDO nodeDO = nodeMapper.getById(id);
        if (ObjectUtils.isEmpty(nodeDO)) {
            throw new MysteriousException(ResponseCodeEnum.NODE_NOT_EXIST);
        }
        if (NodeTypeEnum.SLAVE.getCode().equals(nodeDO.getType())) {
            nodeDO.setStatus(NodeStatusEnum.DISABLED.getCode());
            crudEntity.updateT(nodeDO, userVO);
            log.info("禁用, 更新节点: {}", JSON.toJSONString(nodeDO, true));
            nodeMapper.update(nodeDO);
            /** 关闭jmeter-server进程 */

            /** slave节点指定host关闭jmeter-server */
            String host = nodeDO.getHost();
            Integer port = nodeDO.getPort();
            String username = nodeDO.getUsername();
            String password = nodeDO.getPassword();
            SSHUtils ssh = new SSHUtils(host, port, username, password);

            /** 检查jmeter-server进程 */
            if("null".equals(ssh.execCommand("ps aux | grep jmeter-server | grep -v grep"))) {
                throw new MysteriousException(ResponseCodeEnum.JMETER_SERVER_IS_NOT_ENABLE);
            }

            ssh.execCommand("ps aux | grep jmeter-server | grep -v grep | awk '{print $2}' | xargs kill -9");
            /** 如果进程还在，再关闭一次 */
            if(!"null".equals(ssh.execCommand("ps aux | grep jmeter-server | grep -v grep"))) {
                ssh.execCommand("ps aux | grep jmeter-server | grep -v grep | awk '{print $2}' | xargs kill -9");
            }
        } else {
            throw new MysteriousException(ResponseCodeEnum.ONLY_SLAVE_CAN_DISABLE);
        }
        return true;
    }

    @Override
    public Boolean cronReloadNode(Long id) {
        return true;
    }

    @Override
    public Boolean reloadNode(Long id, UserVO userVO) {
        log.info("重启节点服务: {}", id);
        disableNode(id, userVO);
        enableNode(id, userVO);
        return true;
    }

    @Override
    public Boolean updateNodeStatus(Long id, Integer status) {
        NodeDO nodeDO = nodeMapper.getById(id);
        if (ObjectUtils.isEmpty(nodeDO)) {
            throw new MysteriousException(ResponseCodeEnum.NODE_NOT_EXIST);
        }
        nodeDO.setStatus(status);
        return nodeMapper.update(nodeDO) > 0;
    }
}
