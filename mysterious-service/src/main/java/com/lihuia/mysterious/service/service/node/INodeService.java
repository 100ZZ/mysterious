package com.lihuia.mysterious.service.service.node;

import com.lihuia.mysterious.core.vo.node.NodeVO;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/3/27 10:52 PM
 */

public interface INodeService {

    /**
     * 查询已启用的所有slave节点列表
     * @return
     */
    List<NodeVO> getEnableNodeList();

    /**
     * 详情
     * @param id
     * @return
     */
    NodeVO getById(Long id);
}
