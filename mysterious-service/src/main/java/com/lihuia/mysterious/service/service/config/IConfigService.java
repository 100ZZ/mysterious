package com.lihuia.mysterious.service.service.config;

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
     * @param configVO
     */
    Long addConfig(ConfigVO configVO, UserVO userVO);

    /**
     * 更新配置项
     * @param configVO
     */
    Boolean updateConfig(ConfigVO configVO, UserVO userVO);

    /**
     * 删除配置项，只有创建人能删除
     * @param id
     */
    Boolean deleteConfig(Long id);

    /**
     * 查询分页配置列表
     * @param key
     * @param page
     * @param size
     * @return
     */
    PageVO<ConfigVO> getConfigList(String key, Integer page, Integer size);

    /**
     * 根据配置KEY查询Value
     * @param key
     * @return
     */
    String getValue(String key);
}
