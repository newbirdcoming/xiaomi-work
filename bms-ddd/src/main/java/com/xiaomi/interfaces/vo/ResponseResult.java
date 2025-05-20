package com.xiaomi.interfaces.vo;



import lombok.Data;
import java.io.Serializable;

@Data
public class ResponseResult<T> implements Serializable {
    private String code;
    private String message;
    private T data;

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode("200");
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> ResponseResult<T> success(T data,String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode("200");
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> ResponseResult<T> error(String code, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode("500");
        result.setMessage(message);
        return result;
    }

    public static <T> ResponseResult<T> error(String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode("500");
        result.setMessage(message);
        return result;
    }
    public static <T> ResponseResult<T> success(String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode("200");
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static <T> ResponseResult<T> success() {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode("200");
        result.setMessage("success");
        result.setData(null);
        return result;
    }
}