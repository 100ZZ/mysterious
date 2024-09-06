package com.lihuia.mysterious.service.service.jmx.sample.http.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class HttpParamService implements IHttpParamService {

    @Autowired
    private HttpParamMapper httpParamMapper;

    @Override
    public void addHttpParam(HttpParamDO httpParamDO) {
        httpParamMapper.add(httpParamDO);
    }

    @Override
    public void batchAddHttpParam(List<HttpParamDO> httpParamDOList) {
        httpParamMapper.batchAdd(httpParamDOList);
    }

    @Override
    public List<HttpParamDO> getListByHttpId(Long httpId) {
        return httpParamMapper.getListByHttpId(httpId);
    }

    @Override
    public List<HttpParamDO> getListByJmxId(Long jmxId) {
        return httpParamMapper.getListByJmxId(jmxId);
    }

    @Override
    public void updateHttpParam(HttpParamDO httpParamDO) {
        httpParamMapper.update(httpParamDO);
    }

    @Override
    public void batchUpdateHttpParam(List<HttpParamDO> httpParamDOList) {
        httpParamMapper.batchUpdate(httpParamDOList);
    }

    @Override
    public void deleteHttpParam(Long id) {
        httpParamMapper.delete(id);
    }

    @Override
    public void batchDeleteHttpParam(List<Long> ids) {
        httpParamMapper.batchDelete(ids);
    }

    @Override
    public List<HttpParamDO> getExistParamList(Long httpId, String name) {
        return httpParamMapper.getExistParamList(httpId, name);
    }
}
