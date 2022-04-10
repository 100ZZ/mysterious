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
    NODE_EXIST(1009, "节点已存在", false),
    ID_IS_EMPTY(1010, "主键为空", false),
    CONFIG_EXIST(1011, "配置已存在", false),
    CONFIG_NOT_EXIST(1012, "配置不存在", false),
    NODE_NOT_EXIST(1013, "节点不存在", false),
    JMX_NOT_EXIST(1014, "JMX脚本存在", false),
    CSV_NAME_ERROR(1015, "CSV文件名称异常", false),
    JMX_ERROR(1016, "JMX脚本异常", false),
    JMX_CSV_NAME_ERROR(1017, "CSV Data Set Config控件的Name没有设置", false),
    CSV_IS_EXIST(1018, "CSV文件已存在", false),
    MKDIR_ERROR(1019, "mkDir失败", false),
    RMDIR_ERROR(1020, "rmDir失败", false),
    RMFILE_ERROR(1021, "rmFileE失败", false),
    MKDIR_PARENT_ERROR(1022, "mkDirParent失败", false),
    UPLOAD_FILE_ERROR(1023, "uploadFile失败", false),
    COPY_FILE_ERROR(1024, "copyFile失败", false),
    DOWNLOAD_FILE_ERROR(1025, "downloadFileFromURL失败", false),
    FILE_NOT_EXIST(1026, "文件不存在", false),
    CANNOT_CONNECT(1027, "无法连通", false),
    CLOSE_CONNECT_ERROR(1028, "关闭连接异常", false),
    SSH_EXEC_ERROR(1029, "SSH执行命令异常", false),
    CSV_NOT_EXIST(1030, "CSV文件不存在", false),
    JAR_NAME_ERROR(1031, "JAR名称异常", false),
    JAR_IS_EXIST(1032, "JAR文件已存在", false),
    JAR_NOT_EXIST(1033, "JAR文件不存在", false),
    TESTCASE_HAS_JMX(1034, "用例已经关联了JMX", false),
    JMX_NAME_ERROR(1035, "JMX名称异常", false),
    JMX_HAS_JAR(1036, "JMX关联了JAR", false),
    JMX_HAS_CSV(1037, "JMX关联了CSV", false),
    DOWNLOAD_ERROR(1038, "下载文件失败", false),
    TESTCASE_NAME_ERROR(1039, "用例名称异常", false),
    TESTCASE_IS_EXIST(1040, "用例已经存在", false),
    TESTCASE_NOT_EXIST(1041, "用例不存在", false),
    NODE_IS_ENABLE(1042, "节点启用中, 无法同步", false),






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
