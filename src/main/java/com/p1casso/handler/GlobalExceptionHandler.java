package com.p1casso.handler;


import com.p1casso.Utils.Result;
import com.p1casso.exception.P1cassoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(P1cassoException.class)
    public Result<String> handleP1cassoException(P1cassoException e) {
        log.error(e.getMessage(), e);
        return Result.error(400, e.getMessage());
    }
}
