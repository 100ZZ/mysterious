package com.lihuia.mysterious.service.enums;

/**
 * @author lihuia.com
 * @date 2023/4/16 2:56 PM
 */

public enum ExecTypeEnum {

    DEBUG(1, "调试用例"),
    EXEC(2, "执行用例");

    private Integer type;
    private String message;

    ExecTypeEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {return type;}

    public String getMessage() {return message;}
}
