package com.lihuia.mysterious.service.enums;

/**
 * @author lihuia.com
 * @date 2024/9/9 10:47
 */
public enum JMeterThreadsEnum {


    THREAD_GROUP(0, "ThreadGroup"),
    STEPPING_THREAD_GROUP(1, "SteppingThreadGroup"),
    CONCURRENCY_THREAD_GROUP(2, "ConcurrencyThreadGroup");

    private Integer code;
    private String group;

    JMeterThreadsEnum(Integer code, String group) {
        this.code = code;
        this.group = group;
    }

    public Integer getCode() {
        return code;
    }

    public String getGroup() {
        return group;
    }
}
