package com.lihuia.mysterious.service.service.jmx.sample.http;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

public interface IHttpParamService {

    /**
     * 新增http参数
     * @param httpParamDO
     */
    void addHttpParam(HttpParamDO httpParamDO);

    /**
     * 批量新增
     * @param httpParamDOList
     */
    void batchAddHttpParam(List<HttpParamDO> httpParamDOList);

    /**
     * 根据http sample获取所有的param参数
     * @param httpId
     * @return
     */
    List<HttpParamDO> getListByHttpId(Long httpId);

    /**
     * 根据jmxId获取param
     * @param jmxId
     * @return
     */
    List<HttpParamDO> getListByJmxId(Long jmxId);

    /**
     * 更新http参数
     * @param httpParamDO
     */
    void updateHttpParam(HttpParamDO httpParamDO);

    /**
     * 批量更新
     * @param httpParamDOList
     */
    void batchUpdateHttpParam(List<HttpParamDO> httpParamDOList);

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
    List<HttpParamDO> getExistParamList(Long httpId, String name);
}
