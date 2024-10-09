package com.lihuia.mysterious.core.entity.jmx;

import com.lihuia.mysterious.core.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午3:20
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class JmxDO extends BaseDO<Long> {

    /** 上传前脚本名称 */
    private String srcName;

    /** 上传后脚本名称 */
    private String dstName;

    /** 脚本描述 */
    private String description;

    /** 脚本路径 */
    private String jmxDir;

    /** 脚本关联的用例 */
    private Long testCaseId;

    /** 脚本生成方式, 枚举类：JMeterScriptEnum 0-脚本本地上传, 1-脚本在线编辑 */
    private Integer jmeterScriptType;

    /** 线程组类型, 枚举类：JMeterThreadsEnum 0-ThreadGroup， 1-SteppingThreadGroup, 2-ConcurrentThreadGroup */
    private Integer jmeterThreadsType;

    /** Sample类型, 枚举类：JMeterSampleEnum 0-HTTP Request, 1-Java Sample, 2-Dubbo Request */
    private Integer jmeterSampleType;

}
