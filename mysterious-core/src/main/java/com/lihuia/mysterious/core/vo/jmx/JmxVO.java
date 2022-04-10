package com.lihuia.mysterious.core.vo.jmx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:10
 */

@Data
@ApiModel
public class JmxVO {

    @ApiModelProperty("JMX脚本编号")
    private Long id;

    @ApiModelProperty("上传前JMX文件名称")
    private String srcName;

    @ApiModelProperty("上传后JMX文件名称")
    private String dstName;

    @ApiModelProperty("JMX文件描述")
    private String description;

    @ApiModelProperty("节点")
    private Long nodeId;

    @ApiModelProperty("JMX脚本路径")
    private String jmxDir;

    @ApiModelProperty("JMX脚本关联的用例")
    private Long testCaseId;

    @ApiModelProperty("脚本生成方式, 枚举类：JMeterScriptEnum 0-脚本本地上传, 1-脚本在线编辑")
    private Integer jmeterScriptType;
}
