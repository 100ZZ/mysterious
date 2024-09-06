package com.lihuia.mysterious.service.enums;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */
public enum JMeterSampleEnum {

    HTTP_REQUEST(0, "HTTP Request"),
    DUBBO_SAMPLE(1, "Dubbo Sample"),
    JAVA_REQUEST(2, "Java Request");

    private Integer code;
    private String sample;

    JMeterSampleEnum(Integer code, String sample) {
        this.code = code;
        this.sample = sample;
    }

    public String getSample() {
        return sample;
    }

    public Integer getCode() {
        return code;
    }
}
