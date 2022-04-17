package com.lihuia.mysterious.service.service.node;

import com.lihuia.mysterious.core.vo.node.NodeParam;
import com.lihuia.mysterious.core.vo.node.NodeQuery;
import com.lihuia.mysterious.core.vo.node.NodeVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserVO;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/3/27 10:52 PM
 */

public interface INodeService {

    /**
     * 新增节点
     * @param nodeParam
     * @param userVO
     * @return
     */
    Long addNode(NodeParam nodeParam, UserVO userVO);

    /**
     * 更新节点
     * @param id
     * @param nodeParam
     * @param userVO
     * @return
     */
    Boolean updateNode(Long id, NodeParam nodeParam, UserVO userVO);

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

    /**
     * 删除节点
     * @param id
     * @return
     */
    Boolean deleteNode(Long id);

    /**
     * 分页查询slave节点列表
     * @param query
     * @return
     */
    PageVO<NodeVO> getNodeList(NodeQuery query);

    /**
     * 启用slave节点（jmeter-server服务）
     * @param id
     * @param userVO
     * @return
     */
    Boolean enableNode(Long id, UserVO userVO);

    /**
     * 禁用slave节点（jmeter-server服务）
     * @param id
     * @param userVO
     * @return
     */
    Boolean disableNode(Long id, UserVO userVO);

    /**
     * 定时任务重启slave节点（jmeter-server服务）
     * @param id
     */
    Boolean cronReloadNode(Long id);

    /**
     * 重启slave节点（jmeter-server服务）
     * @param id
     * @param userVO
     * @return
     */
    Boolean reloadNode(Long id, UserVO userVO);

    /**
     * 更新节点状态
     * @param id
     * @param status
     */
    Boolean updateNodeStatus(Long id, Integer status);
}
