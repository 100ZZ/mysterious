package com.lihuia.mysterious.service.enums;

/**
 * @author lihuia.com
 * @date 2023/3/27 10:59 PM
 */
public enum NodeTypeEnum {

    SLAVE(0, "Slave节点"),
    MASTER(1, "Master节点");

    private Integer code;
    private String description;

    NodeTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCode() {
        return code;
    }
}
