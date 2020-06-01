package com.github.brave2chen.springbooteasy.config.advise;

import com.github.brave2chen.springbooteasy.core.RestResponse;
import com.github.brave2chen.springbooteasy.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 *
 * @author brave2chen
 * @date 2020-06-01
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    /**
     * 405 Method Not Allowed 异常
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public RestResponse exception(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return RestResponse.fail(ErrorCode.A0400, "Http Method Not Support");
    }

    /**
     * 404 Not Found 异常
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestResponse onException(NoHandlerFoundException e, HttpServletRequest request) {
        return RestResponse.fail(ErrorCode.C0113);
    }

    /**
     * 500 Internal Server Error 异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse exception(Exception e, HttpServletRequest request) {
        return RestResponse.fail(ErrorCode.B0001);
    }
}
