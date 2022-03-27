package com.lihuia.mysterious.common.response;

/**
 * @author lihuia.com
 * @date 2022/3/27 6:07 PM
 */

public enum ResponseCode {

    SUCCESS(0,"操作成功",true),
    FAIL(-1,"业务异常",false),// 统指一切不好分类的业务异常
    USER_SESSION_LOSS(1000,"用户sesssion丢失",false),
    PARAMS_IS_EMPTY(1001,"参数不能为空",false),
    ILLEGAL_PARAM(1002,"参数不正确",false),
    DATA_EMPTY(1003,"数据为空",false),
    SYSTEM_ERROR(9999,"系统异常",false),
    ;

    private Integer code;
    private String message;
    private Boolean success;

    ResponseCode(Integer code, String message, Boolean success){
        this.code=code;
        this.message=message;
        this.success=success;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }
}
