package com.lihuia.mysterious.core.vo.config;

import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/4/1 上午9:35
 */

@Data
public class ConfigVO {

    private Long id;

    /** 配置字段 */
    private String key;

    /** 字段值 */
    private String value;

    /** 字段描述 */
    private String description;
}
