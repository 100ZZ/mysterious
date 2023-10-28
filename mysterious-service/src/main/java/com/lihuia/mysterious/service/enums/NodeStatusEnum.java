package com.lihuia.mysterious.service.enums;

/**
 * @author maple@lihuia.com
 * @date 2023/3/27 10:58 PM
 */

public enum NodeStatusEnum {

    DISABLED(0, "禁用中"),
    ENABLE(1, "启用中"),
    FAILED(2, "启用失败");

    private Integer code;
    private String description;

    NodeStatusEnum(Integer code, String description) {
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
