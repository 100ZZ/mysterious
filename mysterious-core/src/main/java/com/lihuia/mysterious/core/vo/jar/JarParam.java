package com.lihuia.mysterious.core.vo.jar;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午4:10
 */

@Data
@ApiModel
public class JarParam {

    @ApiModelProperty(value = "JAR文件关联的用例")
    private Long testCaseId;

    @ApiModelProperty(value = "上传的JAR文件")
    private MultipartFile jarFile;
}
