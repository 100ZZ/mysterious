package com.lihuia.mysterious.core.vo.jmx.sample.assertion;

import lombok.Data;

/**
 * @author lihuia.com
 * @date 2024/9/23 09:37
 */

@Data
public class JsonAssertionVO {

    /** 断言的JSON路径 */
    private String jsonPath;

    /** 预期值 */
    private String expectedValue;
}
