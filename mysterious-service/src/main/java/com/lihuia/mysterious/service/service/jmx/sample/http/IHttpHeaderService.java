package com.lihuia.mysterious.service.service.jmx.sample.http;

import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpHeaderVO;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

public interface IHttpHeaderService {

    /**
     * 添加http header
     * @param httpHeaderDO
     */
    void addHttpHeader(HttpHeaderVO httpHeaderDO);

    /**
     * 批量添加http header
     * @param httpHeaderVOList
     */
    void batchAddHttpHeader(List<HttpHeaderVO> httpHeaderVOList);

    /**
     * 根据http id获取所有的header
     * @param httpId
     * @return
     */
    List<HttpHeaderVO> getListByHttpId(Long httpId);

    /**
     * 根据jmx id获取所有的header
     * @param jmxId
     * @return
     */
    List<HttpHeaderVO> getListByJmxId(Long jmxId);

    /**
     * 更新http header
     * @param httpHeaderVO
     */
    void updateHttpHeader(HttpHeaderVO httpHeaderVO);

    /**
     * 批量更新
     * @param httpHeaderVOList
     */
    void batchUpdateHttpHeader(List<HttpHeaderVO> httpHeaderVOList);

    /**
     * 删除
     * @param id
     */
    void deleteHttpHeader(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void batchDeleteHttpHeader(List<Long> ids);

    /**
     * name + value是否已存在
     * @param httpId
     * @param name
     * @return
     */
    List<HttpHeaderVO> getExistHeaderList(Long httpId, String name);
}
