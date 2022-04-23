package com.lihuia.mysterious.core.vo.jar;

import com.lihuia.mysterious.core.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:10
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class JarVO extends BaseVO<Long> {

    @ApiModelProperty(value = "上传前JAR文件名称")
    private String srcName;

    @ApiModelProperty(value = "上传后JAR文件名称")
    private String dstName;

    @ApiModelProperty(value = "JAR文件描述")
    private String description;

    @ApiModelProperty(value = "JAR包路径")
    private String jarDir;

    @ApiModelProperty(value = "JAR文件关联的用例")
    private Long testCaseId;
}
