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
public class TestCaseElementVO {

    @ApiModelProperty("用例编号")
    private Long testCaseId;

    @ApiModelProperty("用例名称")
    private String name;

    @ApiModelProperty("用例目录")
    private String testCaseDir;

    @ApiModelProperty("用例关联的JMX脚本")
    private JmxVO jmxVO;

    @ApiModelProperty("用例关联的CSV文件")
    private List<CsvVO> csvVOList;

    @ApiModelProperty("用例关联的JAR包")
    private List<JarVO> jarVOList;
}
