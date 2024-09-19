package com.lihuia.mysterious.core.vo.testcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lihuia.com
 * @date 2024/9/19 15:08
 */

@ApiModel
public class JMeterResultVO {

    @ApiModelProperty(value = "时间")
    private String currentTime;

    @ApiModelProperty(value = "吞吐量")
    private double throughput;

    @ApiModelProperty(value = "响应时间")
    private double avgResponseTime;

    public JMeterResultVO(String currentTime, double throughput, double avgResponseTime) {
        this.currentTime = currentTime;
        this.throughput = throughput;
        this.avgResponseTime = avgResponseTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public double getThroughput() {
        return throughput;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }
}
