package com.lihuia.mysterious.core.vo.jmx.sample.http;

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
public class HttpVO extends BaseVO<Long> {

    /** 关联的用例 */
    @ApiModelProperty(value = "关联的用例")
    private Long testCaseId;

    /** 关联的JMX脚本 */
    @ApiModelProperty(value = "关联的JMX脚本")
    private Long jmxId;

    /** method HttpMethodEnum 1-GET 2-POST 3-PUT 4-DELETE */
    @ApiModelProperty(value = "method HttpMethodEnum 1-GET 2-POST 3-PUT 4-DELETE")
    private String method;

    /** 协议 HttpProtocolEnum 1-HTTP, 2-HTTPS */
    @ApiModelProperty(value = "协议 HttpProtocolEnum 1-HTTP, 2-HTTPS")
    private String protocol;

    /** domain */
    @ApiModelProperty(value = "domain")
    private String domain;

    /** port */
    @ApiModelProperty(value = "port")
    private String port;

    /** path */
    @ApiModelProperty(value = "path")
    private String path;

    /** content-encoding */
    @ApiModelProperty(value = "content-encoding")
    private String contentEncoding;

    /** head列表 */
    @ApiModelProperty(value = "head列表")
    private List<HttpHeaderVO> httpHeaderVOList;

    /** 参数列表 */
    @ApiModelProperty(value = "参数列表")
    private List<HttpParamVO> httpParamVOList;

    /** body */
    @ApiModelProperty(value = "body")
    private String body;
}
