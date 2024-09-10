package com.lihuia.mysterious.service.enums;

/**
 * @author lihuia.com
 * @date 2023/4/10 11:44 AM
 */

public enum JMeterScriptEnum {
    UPLOAD_JMX(0, "脚本本地上传"),
    ONLINE_JMX(1, "脚本在线编辑")
    ;

    private Integer code;
    private String type;

    JMeterScriptEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

}
