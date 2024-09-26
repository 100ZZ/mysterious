package com.lihuia.mysterious.core.vo.jmx.sample.java;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class JavaVO extends BaseVO<Long> {

    /** 用例 */
    @ApiModelProperty(value = "用例")
    private Long testCaseId;

    /** 脚本 */
    @ApiModelProperty(value = "脚本")
    private Long jmxId;

    /** java request依赖jar包路径 */
    @ApiModelProperty(value = "java request依赖jar包路径")
    private String javaRequestClassPath;

    /** java request的参数 */
    @ApiModelProperty(value = "java request的参数")
    private List<JavaParamVO> javaParamVOList;
}
