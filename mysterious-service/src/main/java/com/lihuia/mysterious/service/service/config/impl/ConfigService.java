package com.lihuia.mysterious.service.service.config.impl;

import com.lihuia.mysterious.common.convert.BeanConverter;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.config.ConfigDO;
import com.lihuia.mysterious.core.mapper.config.ConfigMapper;
import com.lihuia.mysterious.core.vo.config.ConfigQuery;
import com.lihuia.mysterious.core.vo.config.ConfigParam;
import com.lihuia.mysterious.core.vo.config.ConfigVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.crud.CRUDEntity;
import com.lihuia.mysterious.service.service.config.IConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lihuia.com
 * @date 2022/4/1 上午9:48
 */

@Slf4j
@Service
public class ConfigService implements IConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private CRUDEntity<ConfigDO> crudEntity;

    private void checkConfigParam(ConfigParam configParam) {
        if (ObjectUtils.isEmpty(configParam)) {
            throw new MysteriousException(ResponseCodeEnum.PARAMS_EMPTY);
        }
        if (StringUtils.isEmpty(configParam.getKey())
                || StringUtils.isEmpty(configParam.getValue())
                || StringUtils.isEmpty(configParam.getDescription())) {
            throw new MysteriousException(ResponseCodeEnum.PARAM_MISSING);
        }
    }

    private void checkConfigExist(ConfigParam configParam) {
        List<ConfigDO> configList = configMapper.getByKey(configParam.getKey());
        if (!CollectionUtils.isEmpty(configList)) {
            throw new MysteriousException(ResponseCodeEnum.CONFIG_EXIST);
        }
    }

    @Override
    public Long addConfig(ConfigParam configParam, UserVO userVO) {
        checkConfigParam(configParam);
        checkConfigExist(configParam);
        ConfigDO configDO = BeanConverter.doSingle(configParam, ConfigDO.class);
        crudEntity.addT(configDO, userVO);
        configMapper.add(configDO);
        return configDO.getId();
    }

    @Override
    public Boolean updateConfig(Long id, ConfigParam configParam, UserVO userVO) {
        ConfigDO configDO = configMapper.getById(id);
        if (ObjectUtils.isEmpty(configDO)) {
            return false;
        }
        configDO = BeanConverter.doSingle(configParam, ConfigDO.class);
        configDO.setId(id);
        crudEntity.updateT(configDO, userVO);
        return configMapper.update(configDO) > 0;
    }

    @Override
    public Boolean deleteConfig(Long id) {
        ConfigDO configDO = configMapper.getById(id);
        if (ObjectUtils.isEmpty(configDO)) {
            return false;
        }
        return configMapper.delete(id) > 0;
    }

    @Override
    public PageVO<ConfigVO> getConfigList(ConfigQuery query) {
        PageVO<ConfigVO> pageVO = new PageVO<>();
        Integer offset = pageVO.getOffset(query.getPage(), query.getSize());
        Integer total = configMapper.getConfigCount(query.getKey());
        if (total.compareTo(0) > 0) {
            pageVO.setTotal(total);
            List<ConfigDO> configList = configMapper.getConfigList(query.getKey(), offset, query.getSize());
            pageVO.setList(configList.stream().map(configDO -> {
                ConfigVO configVO = BeanConverter.doSingle(configDO, ConfigVO.class);
                configVO.setId(configDO.getId());
                return configVO;
            }).collect(Collectors.toList()));
        }
        return pageVO;
    }

    @Override
    public String getValue(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new MysteriousException(ResponseCodeEnum.CONFIG_NOT_EXIST);
        }
        return configMapper.getValue(key);
    }
}
