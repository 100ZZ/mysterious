package com.lihuia.mysterious.core.vo.jar;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:10
 */

@Data
@ApiModel
public class JarVO {

    @ApiModelProperty("Jar依赖编号")
    private Long id;

    @ApiModelProperty("上传前JAR文件名称")
    private String srcName;

    @ApiModelProperty("上传后JAR文件名称")
    private String dstName;

    @ApiModelProperty("JAR文件描述")
    private String description;

    @ApiModelProperty("节点")
    private Long nodeId;

    @ApiModelProperty("JAR包路径")
    private String testCaseJarDir;

    @ApiModelProperty("JAR文件关联的用例")
    private Long testCaseId;
}
