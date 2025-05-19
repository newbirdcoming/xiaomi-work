package com.xiaomi.interfaces.exception;

public enum ExceptionType {
    // 车辆相关异常
    VEHICLE_NOT_FOUND("40401", "车辆未找到"),
    VEHICLE_DATA_INVALID("40402", "车辆数据无效"),
    // 规则相关异常
    RULE_NOT_FOUND("40001", "规则未找到"),
    RULE_CONDITION_INVALID("40002", "解析失败"),
    RULE_INVALID("40003", "规则无效"),
    // 其他通用异常
    SYSTEM_ERROR("50001", "系统错误"),
    INTERNAL_SERVER_ERROR("50002", "内部服务器错误");
    //

    private final String errorCode;
    private final String errorMessage;

    ExceptionType(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}