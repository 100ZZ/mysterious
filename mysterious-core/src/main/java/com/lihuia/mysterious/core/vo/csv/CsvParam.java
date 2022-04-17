package com.lihuia.mysterious.core.vo.csv;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:10
 */

@Data
@ApiModel
public class CsvParam {

    @ApiModelProperty(value = "CSV文件关联的用例")
    private Long testCaseId;

    @ApiModelProperty(value = "上传的CSV文件")
    private MultipartFile csvFile;
}
