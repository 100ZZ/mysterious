package com.lihuia.mysterious.core.vo.testcase;

import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/2 11:53 AM
 */

@Data
@ApiModel
public class TestCaseFullVO {

    @ApiModelProperty("用例编号")
    private Long id;

    @ApiModelProperty("用例名称")
    private String name;

    @ApiModelProperty("用例描述")
    private String description;

    @ApiModelProperty("业务线")
    private String biz;

    @ApiModelProperty("服务")
    private String service;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("执行状态; 0-未执行，1-执行中, 2-执行成功, 3-执行异常, 4-等待执行, 5-排队取消")
    private Integer status;

    @ApiModelProperty("用例目录")
    private String testCaseDir;

    @ApiModelProperty("用例关联的JMX脚本")
    private JmxVO jmxVO;

    @ApiModelProperty("用例关联的CSV文件")
    private List<CsvVO> csvVOList;

    @ApiModelProperty("用例关联的JAR包")
    private List<JarVO> jarVOList;
}
