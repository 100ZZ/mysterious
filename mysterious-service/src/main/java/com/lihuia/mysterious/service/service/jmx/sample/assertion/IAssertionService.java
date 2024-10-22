package com.lihuia.mysterious.service.service.jmx.sample.assertion;

import com.lihuia.mysterious.core.vo.jmx.sample.assertion.AssertionVO;

/**
 * @author lihuia.com
 * @date 2024/9/23 10:53
 */
public interface IAssertionService {

    /**
     * 添加断言
     * @param assertionVO
     */
    Long addAssertion(AssertionVO assertionVO);

    /**
     * 更新断言
     * @param assertionVO
     */
    void updateAssertion(AssertionVO assertionVO);

    /**
     * 删除断言
     * @param id
     */
    void deleteAssertion(Long id);

    /**
     * 根据JMX ID获取断言
     * @param jmxId
     * @return
     */
    AssertionVO getByJmxId(Long jmxId);

    void deleteByJmxId(Long jmxId);
}
