//package com.xiaomi.interfaces.exception;
//
//
//import com.xiaomi.interfaces.vo.ResponseResult;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(DMSException.class)
//    public ResponseResult<?> handleDMSException(DMSException e) {
//        // 假设 ResponseResult.error() 方法接受两个参数：错误码和消息
//        return ResponseResult.error(
//                e.getErrorCode(),
//                e.getMessage()
//        );
//    }
//
//
//    @ExceptionHandler(VehicleNotFoundException.class)
//    public ResponseResult<?> handleVehicleNotFound(VehicleNotFoundException e) {
//        return ResponseResult.error(404, e.getMessage());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseResult<?> handleGlobalException(Exception e) {
//        return ResponseResult.error(500, "系统繁忙，请稍后再试");
//    }
//
//    @ExceptionHandler(RuleNotFoundException.class)
//    public ResponseResult<?> handleRuleNotFound(RuleNotFoundException e) {
//        return ResponseResult.error(404, e.getMessage());
//    }
//
//
//}