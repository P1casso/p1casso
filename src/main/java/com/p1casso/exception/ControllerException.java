package com.p1casso.exception;

import com.p1casso.Utils.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@ControllerAdvice
public class ControllerException extends RuntimeException {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidException(MethodArgumentNotValidException exception) {
        String exceptionMessage = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        return Result.error(400, exceptionMessage);
    }
}
