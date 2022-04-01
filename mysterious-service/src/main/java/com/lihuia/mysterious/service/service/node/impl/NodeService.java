package com.lihuia.mysterious.service.service.node.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.mapper.node.NodeMapper;
import com.lihuia.mysterious.core.entity.node.NodeDO;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.enums.NodeStatusEnum;
import com.lihuia.mysterious.service.service.node.INodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    @Autowired
    private CRUDEntity<NodeDO> crudEntity;

    private void checkNodeParam(NodeVO nodeVO) {
        if (ObjectUtils.isEmpty(nodeVO)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        if (StringUtils.isEmpty(nodeVO.getHost())
                || StringUtils.isEmpty(nodeVO.getUsername())
                || ObjectUtils.isEmpty(nodeVO.getPort())
                || ObjectUtils.isEmpty(nodeVO.getType()) ) {
            throw new MysteriousException(ResponseCodeEnum.PARAM_MISSING);
        }
    }

    private void checkNodeExist(NodeVO nodeVO) {
        List<NodeDO> nodeList = nodeMapper.getByHost(nodeVO.getHost());
        if (!CollectionUtils.isEmpty(nodeList)) {
            throw new MysteriousException(ResponseCodeEnum.NODE_EXIST);
        }
    }

    @Override
    public Long addNode(NodeVO nodeVO, UserVO userVO) {
        checkNodeParam(nodeVO);
        checkNodeExist(nodeVO);
        NodeDO nodeDO = BeanConverter.doSingle(nodeVO, NodeDO.class);
        nodeDO.setStatus(NodeStatusEnum.DISABLED.getCode());
        crudEntity.addT(nodeDO, userVO);
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
        nodeDO = BeanConverter.doSingle(nodeVO, NodeDO.class);
        crudEntity.updateT(nodeDO, userVO);
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
        NodeVO nodeVO = BeanConverter.doSingle(nodeDO, NodeVO.class);
        nodeVO.setId(id);
        nodeVO.setPassword("******");
        return nodeVO;
    }
}
