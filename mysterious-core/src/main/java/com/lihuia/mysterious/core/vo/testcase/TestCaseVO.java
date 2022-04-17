package com.lihuia.mysterious.core.vo.testcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:20
 */

@Data
@ApiModel
public class TestCaseVO {

    @ApiModelProperty(value = "JMX脚本编号")
    private Long id;

    @ApiModelProperty(value = "用例名称")
    private String name;

    @ApiModelProperty(value = "用例描述")
    private String description;

    @ApiModelProperty(value = "业务线")
    private String biz;

    @ApiModelProperty(value = "服务")
    private String service;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "执行状态; 0-未执行，1-执行中, 2-执行成功, 3-执行异常, 4-等待执行, 5-排队取消")
    private Integer status;

    @ApiModelProperty(value = "用例目录")
    private String testCaseDir;
}
