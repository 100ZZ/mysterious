package com.lihuia.mysterious.core.vo.jmx;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author maple@lihuia.com
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
}
