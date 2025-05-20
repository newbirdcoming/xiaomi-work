package com.xiaomi.interfaces.exception;


import com.xiaomi.interfaces.vo.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DMSException.class)
    public ResponseResult<?> handleDMSException(DMSException e) {
        return ResponseResult.error(
                e.getErrorCode(),  // 从异常中获取业务错误码
                e.getMessage()     // 从异常中获取错误消息
        );
    }

}