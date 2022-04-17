package com.lihuia.mysterious.service.service.config;

import com.lihuia.mysterious.core.vo.config.ConfigQuery;
import com.lihuia.mysterious.core.vo.config.ConfigParam;
import com.lihuia.mysterious.core.vo.config.ConfigVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserVO;

/**
 * @author lihuia.com
 * @date 2022/4/1 上午9:48
 */
public interface IConfigService {

    /**
     * 新增配置项
     * @param configParam
     * @param userVO
     * @return
     */
    Long addConfig(ConfigParam configParam, UserVO userVO);

    /**
     * 更新配置项
     * @param id
     * @param ConfigParam
     * @param userVO
     * @return
     */
    Boolean updateConfig(Long id, ConfigParam ConfigParam, UserVO userVO);

    /**
     * 删除配置项，只有创建人能删除
     * @param id
     */
    Boolean deleteConfig(Long id);

    /**
     * 查询分页配置列表
     * @param query
     * @return
     */
    PageVO<ConfigVO> getConfigList(ConfigQuery query);

    /**
     * 根据配置KEY查询Value
     * @param key
     * @return
     */
    String getValue(String key);
}
