package com.lihuia.mysterious.core.vo.jmx;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import com.lihuia.mysterious.core.vo.jmx.sample.assertion.AssertionVO;
import com.lihuia.mysterious.core.vo.jmx.sample.csv.CsvDataVO;
import com.lihuia.mysterious.core.vo.jmx.sample.dubbo.DubboVO;
import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpVO;
import com.lihuia.mysterious.core.vo.jmx.sample.java.JavaVO;
import com.lihuia.mysterious.core.vo.jmx.thread.ConcurrencyThreadGroupVO;
import com.lihuia.mysterious.core.vo.jmx.thread.SteppingThreadGroupVO;
import com.lihuia.mysterious.core.vo.jmx.thread.ThreadGroupVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午4:10
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class JmxVO extends BaseVO<Long> {

    @ApiModelProperty(value = "上传前JMX文件名称")
    private String srcName;

    @ApiModelProperty(value = "上传后JMX文件名称")
    private String dstName;

    @ApiModelProperty(value = "JMX文件描述")
    private String description;

    @ApiModelProperty(value = "JMX脚本路径")
    private String jmxDir;

    @ApiModelProperty(value = "JMX脚本关联的用例")
    private Long testCaseId;

    @ApiModelProperty(value = "脚本生成方式, 枚举类：JMeterScriptEnum 0-脚本本地上传, 1-脚本在线编辑")
    private Integer jmeterScriptType;

    /** 线程组类型, 枚举类：JMeterThreadsEnum 0-ThreadGroup， 1-SteppingThreadGroup, 2-ConcurrencyThreadGroup */
    @ApiModelProperty(value = "线程组类型, 枚举类：JMeterThreadsEnum 0-ThreadGroup， 1-SteppingThreadGroup, 2-ConcurrencyThreadGroup")
    private Integer jmeterThreadsType;

    /** ThreadGroup 结构体编辑, jmeterThreadsType = 0 */
    @ApiModelProperty(value = "ThreadGroup 结构体编辑, jmeterThreadsType = 0")
    private ThreadGroupVO threadGroupVO;

    /** SteppingThreadGroup 结构体编辑, jmeterThreadsType = 1 */
    @ApiModelProperty(value = "SteppingThreadGroup 结构体编辑, jmeterThreadsType = 1")
    private SteppingThreadGroupVO steppingThreadGroupVO;

    /** ConcurrencyThreadGroup 结构体编辑, jmeterThreadsType = 2 */
    @ApiModelProperty(value = "ConcurrencyThreadGroup 结构体编辑, jmeterThreadsType = 2")
    private ConcurrencyThreadGroupVO concurrencyThreadGroupVO;

    /** Sample类型, 枚举类：JMeterSampleEnum 0-HTTP Request, 1-Java Sample, 2-Dubbo Request */
    @ApiModelProperty(value = "Sample类型, 枚举类：JMeterSampleEnum 0-HTTP Request, 1-Java Sample, 2-Dubbo Request")
    private Integer jmeterSampleType;

    /** HttpVO结构体编辑, jmeterSampleType = 0 */
    @ApiModelProperty(value = "HttpVO结构体编辑, jmeterSampleType = 0")
    private HttpVO httpVO;

    /** DubboVO结构体编辑, jmeterSampleType = 1 */
    @ApiModelProperty(value = "DubboVO结构体编辑, jmeterSampleType = 1")
    private DubboVO dubboVO;

    /** JavaVO结构体编辑, jmeterSampleType = 2 */
    @ApiModelProperty(value = "JavaVO结构体编辑, jmeterSampleType = 2")
    private JavaVO javaVO;

    @ApiModelProperty(value = "断言")
    private AssertionVO assertionVO;

    @ApiModelProperty(value = "CSV文件模块")
    private CsvDataVO csvDataVO;
}
