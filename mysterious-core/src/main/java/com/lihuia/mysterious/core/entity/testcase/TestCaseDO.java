package com.lihuia.mysterious.core.entity.testcase;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import com.lihuia.mysterious.core.entity.csv.CsvDO;
import com.lihuia.mysterious.core.entity.jar.JarDO;
import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author maple@lihuia.com
 * @date 2023/4/1 下午3:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TestCaseDO extends BaseDO<Long> {

    /** 用例名称 */
    private String name;

    /** 用例描述 */
    private String description;

    /** 业务线 */
    private String biz;

    /** 服务 */
    private String service;

    /** 版本号 */
    private String version;

    /** 执行状态; 0-未执行，1-执行中, 2-执行成功, 3-执行异常, 4-等待执行, 5-排队取消 */
    private Integer status;

    /** 用例目录 */
    private String testCaseDir;

    /** 用例关联的JMX脚本 */
    private JmxDO jmxDO;

    /** 用例关联的CSV文件 */
    private List<CsvDO> csvDOList;

    /** 用例关联的JAR包 */
    private List<JarDO> jarDOList;
}
