package com.lihuia.mysterious.service.service.jmx.sample.dubbo;

import com.lihuia.mysterious.core.vo.jmx.sample.dubbo.DubboMethodArgsVO;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/10/21 11:23
 */
public interface IDubboMethodArgsService {

    void addDubboMethodArgs(DubboMethodArgsVO dubboMethodArgsVO);

    List<DubboMethodArgsVO> getListByDubboId(Long dubboId);

    List<DubboMethodArgsVO> getListByJmxId(Long jmxId);

    void deleteDubboMethodArgs(Long id);

    void batchDeleteDubboMethodArgs(List<Long> ids);

    List<DubboMethodArgsVO> getExistMethodArgsList(Long dubboId, String paramType);
}
