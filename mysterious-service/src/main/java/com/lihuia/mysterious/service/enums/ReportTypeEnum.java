package com.lihuia.mysterious.service.enums;

/**
 * @author lihuia.com
 * @date 2022/3/27 10:58 PM
 */

public enum ReportTypeEnum {

    DEBUG(1, "调试"),
    RUNNING(2, "执行");

    private Integer code;
    private String description;

    ReportTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
