package com.xiaomi.interfaces.exception;
public class DMSException extends RuntimeException {
    private final String errorCode; // 业务错误码

    public DMSException(String errorCode, String message) {
        super(message); // 调用父类构造函数，存储错误消息
        this.errorCode = errorCode; // 存储业务错误码
    }

    public String getErrorCode() {
        return errorCode;
    }
}