package com.xiaomi.interfaces.exception;

public enum ExceptionType {
    // 车辆相关异常
    VEHICLE_NOT_FOUND("40401", "车辆未找到"),
    // 规则相关异常
    RULE_PARSE_FAIL("40501", "解析失败"),
    RULE_NOT_FOUND("40502", "规则未找到"),
    // 信号相关异常
    SIGNAL_NOT_FOUND("40601", "信号未找到"),




    // 系统相关异常
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