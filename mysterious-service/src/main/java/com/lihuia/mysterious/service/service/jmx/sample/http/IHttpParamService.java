package com.lihuia.mysterious.service.service.jmx.sample.http;

import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpParamVO;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

public interface IHttpParamService {

    /**
     * 新增http参数
     * @param httpParamVO
     */
    void addHttpParam(HttpParamVO httpParamVO);

    /**
     * 批量新增
     * @param httpParamVOList
     */
    void batchAddHttpParam(List<HttpParamVO> httpParamVOList);

    /**
     * 根据http sample获取所有的param参数
     * @param httpId
     * @return
     */
    List<HttpParamVO> getListByHttpId(Long httpId);

    /**
     * 根据jmxId获取param
     * @param jmxId
     * @return
     */
    List<HttpParamVO> getListByJmxId(Long jmxId);

    /**
     * 更新http参数
     * @param httpParamVO
     */
    void updateHttpParam(HttpParamVO httpParamVO);

    /**
     * 批量更新
     * @param httpParamVOList
     */
    void batchUpdateHttpParam(List<HttpParamVO> httpParamVOList);

    /**
     * 删除
     * @param id
     */
    void deleteHttpParam(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void batchDeleteHttpParam(List<Long> ids);

    /**
     * 查找是否已存在
     * @param httpId
     * @param name
     * @return
     */
    List<HttpParamVO> getExistParamList(Long httpId, String name);
}
