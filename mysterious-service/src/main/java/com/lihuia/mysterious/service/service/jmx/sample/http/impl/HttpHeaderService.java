package com.lihuia.mysterious.service.service.jmx.sample.http.impl;

import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpHeaderDO;
import com.lihuia.mysterious.service.service.jmx.sample.http.IHttpHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class HttpHeaderService implements IHttpHeaderService {

    @Autowired
    private HttpHeaderMapper httpHeaderMapper;

    @Override
    public void addHttpHeader(HttpHeaderDO httpHeaderDO) {
        httpHeaderMapper.add(httpHeaderDO);
    }

    @Override
    public void batchAddHttpHeader(List<HttpHeaderDO> httpHeaderDOList) {
        httpHeaderMapper.batchAdd(httpHeaderDOList);
    }

    @Override
    public List<HttpHeaderDO> getListByHttpId(Long httpId) {
        return httpHeaderMapper.getListByHttpId(httpId);
    }

    @Override
    public List<HttpHeaderDO> getListByJmxId(Long jmxId) {
        return httpHeaderMapper.getListByJmxid(jmxId);
    }

    @Override
    public void updateHttpHeader(HttpHeaderDO httpHeaderDO) {
        httpHeaderMapper.update(httpHeaderDO);
    }

    @Override
    public void batchUpdateHttpHeader(List<HttpHeaderDO> httpHeaderDOList) {
        httpHeaderMapper.batchUpdate(httpHeaderDOList);
    }

    @Override
    public void deleteHttpHeader(Long id) {
        httpHeaderMapper.delete(id);
    }

    @Override
    public void batchDeleteHttpHeader(List<Long> ids) {
        httpHeaderMapper.batchDelete(ids);
    }

    @Override
    public List<HttpHeaderDO> getExistHeaderList(Long httpId, String name) {
        return httpHeaderMapper.getExistHeaderList(httpId, name);
    }
}
