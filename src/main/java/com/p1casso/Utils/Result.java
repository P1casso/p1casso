package com.p1casso.Utils;

import lombok.Data;

@Data
public class Result<T> {

    private int code;

    private String message;

    private T data;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }

    /**
     * 失败
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }

    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(404, "失败");
    }
}
