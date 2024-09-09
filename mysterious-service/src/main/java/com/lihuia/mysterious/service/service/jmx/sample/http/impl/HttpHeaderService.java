package com.lihuia.mysterious.service.service.jmx.sample.http.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpHeaderDO;
import com.lihuia.mysterious.core.mapper.jmx.sample.http.HttpHeaderMapper;
import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpHeaderVO;
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
    public void addHttpHeader(HttpHeaderVO httpHeaderVO) {
        httpHeaderMapper.add(BeanConverter.doSingle(httpHeaderVO, HttpHeaderDO.class));
    }

    @Override
    public void batchAddHttpHeader(List<HttpHeaderVO> httpHeaderVOList) {
        httpHeaderMapper.batchAdd(BeanConverter.doList(httpHeaderVOList, HttpHeaderDO.class));
    }

    @Override
    public List<HttpHeaderVO> getListByHttpId(Long httpId) {
        return BeanConverter.doList(httpHeaderMapper.getListByHttpId(httpId), HttpHeaderVO.class);
    }

    @Override
    public List<HttpHeaderVO> getListByJmxId(Long jmxId) {
        return BeanConverter.doList(httpHeaderMapper.getListByJmxId(jmxId), HttpHeaderVO.class);
    }

    @Override
    public void updateHttpHeader(HttpHeaderVO httpHeaderVO) {
        httpHeaderMapper.update(BeanConverter.doSingle(httpHeaderVO, HttpHeaderDO.class));
    }

    @Override
    public void batchUpdateHttpHeader(List<HttpHeaderVO> httpHeaderVOList) {
        httpHeaderMapper.batchUpdate(BeanConverter.doList(httpHeaderVOList, HttpHeaderDO.class));
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
    public List<HttpHeaderVO> getExistHeaderList(Long httpId, String name) {
        return BeanConverter.doList(httpHeaderMapper.getExistHeaderList(httpId, name), HttpHeaderVO.class);
    }
}
