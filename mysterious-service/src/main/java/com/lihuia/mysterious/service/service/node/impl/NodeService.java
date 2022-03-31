package com.lihuia.mysterious.service.service.node.impl;

import com.lihuia.mysterious.common.convert.CommonBeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.mapper.node.NodeMapper;
import com.lihuia.mysterious.core.entity.node.NodeDO;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.enums.NodeStatusEnum;
import com.lihuia.mysterious.service.service.node.INodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/3/27 10:55 PM
 */

@Slf4j
@Service
public class NodeService implements INodeService {

    @Autowired
    private NodeMapper nodeMapper;

    private void checkNodeParam(NodeVO nodeVO) {
        if (ObjectUtils.isEmpty(nodeVO)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        if (StringUtils.isBlank(nodeVO.getHost())
                || StringUtils.isBlank(nodeVO.getUsername())
                || ObjectUtils.isEmpty(nodeVO.getPort())
                || ObjectUtils.isEmpty(nodeVO.getType()) ) {
            throw new MysteriousException(ResponseCodeEnum.PARAM_MISSING);
        }
    }

    private void checkNodeExist(NodeVO nodeVO) {
        String host = nodeVO.getHost();
        List<NodeDO> nodeList = nodeMapper.getByHost(host);
        if (!CollectionUtils.isEmpty(nodeList)) {
            throw new MysteriousException(ResponseCodeEnum.NODE_EXIST);
        }
    }

    @Override
    public Long addNode(NodeVO nodeVO, UserVO userVO) {
        checkNodeParam(nodeVO);
        checkNodeExist(nodeVO);
        NodeDO nodeDO = CommonBeanConverter.doSingle(nodeVO, NodeDO.class);
        nodeDO.setCreator(userVO.getUsername());
        nodeDO.setCreatorId(userVO.getId());
        nodeDO.setCreateTime(LocalDateTime.now());
        nodeDO.setStatus(NodeStatusEnum.DISABLED.getCode());
        nodeMapper.add(nodeDO);
        return nodeDO.getId();
    }

    @Override
    public Boolean updateNode(NodeVO nodeVO, UserVO userVO) {
        if (ObjectUtils.isEmpty(nodeVO.getId())) {
            throw new MysteriousException(ResponseCodeEnum.ID_IS_EMPTY);
        }
        NodeDO nodeDO = nodeMapper.getById(nodeVO.getId());
        if (ObjectUtils.isEmpty(nodeDO)) {
            return false;
        }
        log.info("nodeDO:{}", nodeDO);
        nodeDO = CommonBeanConverter.doSingle(nodeVO, NodeDO.class);
        nodeDO.setId(nodeVO.getId());
        nodeDO.setModifier(userVO.getUsername());
        nodeDO.setModifierId(userVO.getId());
        nodeDO.setModifyTime(LocalDateTime.now());
        return nodeMapper.update(nodeDO) > 0;
    }

    @Override
    public List<NodeVO> getEnableNodeList() {
        return new ArrayList<>();
    }

    @Override
    public NodeVO getById(Long id) {
        NodeDO nodeDO = nodeMapper.getById(id);
        if (ObjectUtils.isEmpty(nodeDO)) {
            return null;
        }
        NodeVO nodeVO = CommonBeanConverter.doSingle(nodeDO, NodeVO.class);
        nodeVO.setId(id);
        return nodeVO;
    }
}
