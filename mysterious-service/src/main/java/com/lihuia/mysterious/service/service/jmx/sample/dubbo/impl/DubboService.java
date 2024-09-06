package com.lihuia.mysterious.service.service.jmx.sample.dubbo.impl;

import com.lihuia.mysterious.core.entity.jmx.sample.dubbo.DubboDO;
import com.lihuia.mysterious.service.service.jmx.sample.dubbo.IDubboService;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Service
public class DubboService implements IDubboService {
    @Override
    public DubboDO getByJmxId(Long jmxId) {
        return null;
    }

    @Override
    public void addDubbo(DubboDO dubboDO) {

    }
}
