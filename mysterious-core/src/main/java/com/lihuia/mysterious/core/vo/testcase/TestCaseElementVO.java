package com.lihuia.mysterious.core.vo.testcase;

import com.lihuia.mysterious.core.entity.csv.CsvDO;
import com.lihuia.mysterious.core.entity.jar.JarDO;
import com.lihuia.mysterious.core.entity.jmx.JmxDO;
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

    @ApiModelProperty("用例关联的JMX脚本")
    private JmxDO jmxDO;

    @ApiModelProperty("用例关联的CSV文件")
    private List<CsvDO> csvDOList;

    @ApiModelProperty("用例关联的JAR包")
    private List<JarDO> jarDOList;
}
