package com.lihuia.mysterious.service.enums;

/**
 * @author maple@lihuia.com
 * @date 2023/4/10 5:14 PM
 */

public enum TestCaseStatus {
    NOT_RUN(0, "没有执行"),
    RUN_ING(1, "正执行中"),
    RUN_SUCCESS(2, "执行成功"),
    RUN_FAILED(3, "执行异常"),
    RUN_WAITING(4, "等待执行"),
    WAIT_CANCEL(5, "排队取消");

    private Integer code;
    private String description;

    TestCaseStatus(Integer code, String description) {
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
