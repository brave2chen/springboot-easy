package com.github.brave2chen.springbooteasy.config.advise;

import com.github.brave2chen.springbooteasy.core.RestResponse;
import com.github.brave2chen.springbooteasy.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

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
     * Assert 异常处理
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse onException(IllegalArgumentException e, HttpServletRequest request) {
        return logResponse(RestResponse.fail(ErrorCode.A0400, e.getMessage()), e, request);
    }

    /**
     * ConstraintViolation 异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse onException(ConstraintViolationException e, HttpServletRequest request) {
        String resultMsg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.joining("; "));
        return logResponse(RestResponse.fail(ErrorCode.A0400, resultMsg), e, request);
    }

    /**
     * <code>@Valid</code> 异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse onException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String resultMsg = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
        return logResponse(RestResponse.fail(ErrorCode.A0400, resultMsg), e, request);
    }

    /**
     * <code>@RestController</code> <code>@RequestParam</code> 参数的 <code>@Valid</code> 异常处理
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse onException(BindException e, HttpServletRequest request) {
        String resultMsg = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
        return logResponse(RestResponse.fail(ErrorCode.A0400, resultMsg), e, request);
    }

    /**
     * HttpMediaTypeNotSupportedException 异常
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse exception(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        return logResponse(RestResponse.fail(ErrorCode.A0400, e.getMessage()), e, request);
    }

    /**
     * HttpMessageNotReadableException 异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse exception(HttpMessageNotReadableException e, HttpServletRequest request) {
        return logResponse(RestResponse.fail(ErrorCode.A0400, "Required request body is missing"), e, request);
    }

    /**
     * 405 Method Not Allowed 异常
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public RestResponse exception(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return logResponse(RestResponse.fail(ErrorCode.A0400, e.getMessage()), e, request);
    }

    /**
     * 404 Not Found 异常
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestResponse onException(NoHandlerFoundException e, HttpServletRequest request) {
        return logResponse(RestResponse.fail(ErrorCode.C0113), e, request);
    }

    /**
     * 401 Unauthorized 异常
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public RestResponse onException(AccessDeniedException e, HttpServletRequest request) {
        return logResponse(RestResponse.fail(ErrorCode.A0301), e, request);
    }

    /**
     * 500 Internal Server Error 异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse exception(Exception e, HttpServletRequest request) {
        return logResponse(RestResponse.fail(ErrorCode.B0001), e, request);
    }

    private RestResponse logResponse(RestResponse response, Exception e, HttpServletRequest request) {
        log.error("Rest-URI: " + request.getRequestURI() + ", Exception: " + e.getMessage(), e);
        return response;
    }
}
