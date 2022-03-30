package com.lihuia.mysterious.common.response;

/**
 * @author lihuia.com
 * @date 2022/3/29 11:02 PM
 */

public enum ResponseCodeEnum {

    SUCCESS(0,"操作成功",true),
    FAIL(-1,"业务异常",false),// 统指一切不好分类的业务异常
    USER_SESSION_LOSS(1000,"用户sesssion丢失",false),
    PARAMS_EMPTY(1001,"参数不能为空",false),
    PARAM_WRONG(1002,"参数不正确",false),
    PARAM_MISSING(1003,"参数缺失",false),
    USER_EXIST(1004, "用户已存在", false),
    USER_NOT_EXIST(1005, "用户不存在", false),
    USER_PASSWORD_ERROR(1006, "用户密码错误", false),
    USER_NOT_LOGIN(1007, "用户未登录", false),
    USER_TOKEN_EXPIRE(1008, "用户凭证失效", false),

    SYSTEM_ERROR(9999,"系统异常",false),
    ;

    private Integer code;
    private String message;
    private Boolean success;

    ResponseCodeEnum(Integer code, String message, Boolean success){
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
