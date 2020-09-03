package com.github.brave2chen.springbooteasy.config.advise;

import com.diboot.core.exception.BusinessException;
import com.diboot.core.vo.JsonResult;
import com.diboot.core.vo.Status;
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
    public JsonResult onException(IllegalArgumentException e, HttpServletRequest request) {
        return logResponse(JsonResult.FAIL_INVALID_PARAM(e.getMessage()), e, request);
    }

    /**
     * ConstraintViolation 异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public JsonResult onException(ConstraintViolationException e, HttpServletRequest request) {
        String resultMsg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.joining("; "));
        return logResponse(JsonResult.FAIL_INVALID_PARAM(resultMsg), e, request);
    }

    /**
     * <code>@Valid</code> 异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public JsonResult onException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String resultMsg = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
        return logResponse(JsonResult.FAIL_INVALID_PARAM(resultMsg), e, request);
    }

    /**
     * <code>@RestController</code> <code>@RequestParam</code> 参数的 <code>@Valid</code> 异常处理
     */
    @ExceptionHandler(value = BindException.class)
    public JsonResult onException(BindException e, HttpServletRequest request) {
        String resultMsg = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
        return logResponse(JsonResult.FAIL_INVALID_PARAM(resultMsg), e, request);
    }

    /**
     * HttpMediaTypeNotSupportedException 异常
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public JsonResult exception(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        return logResponse(JsonResult.FAIL_INVALID_PARAM(e.getMessage()), e, request);
    }

    /**
     * HttpMessageNotReadableException 异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public JsonResult exception(HttpMessageNotReadableException e, HttpServletRequest request) {
        return logResponse(JsonResult.FAIL_INVALID_PARAM("Required request body is missing"), e, request);
    }

    /**
     * 405 Method Not Allowed 异常
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public JsonResult exception(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return logResponse(JsonResult.FAIL_INVALID_PARAM(e.getMessage()), e, request);
    }

    /**
     * 404 Not Found 异常
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public JsonResult onException(NoHandlerFoundException e, HttpServletRequest request) {
        return logResponse(JsonResult.FAIL_NOT_FOUND(Status.FAIL_NOT_FOUND.label()), e, request);
    }

    /**
     * 401 Unauthorized 异常
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public JsonResult onException(AccessDeniedException e, HttpServletRequest request) {
        return logResponse(JsonResult.FAIL_INVALID_TOKEN(Status.FAIL_INVALID_TOKEN.label()), e, request);
    }

    /**
     * 500 Internal Server Error 异常
     */
    @ExceptionHandler(value = Exception.class)
    public JsonResult exception(Exception e, HttpServletRequest request) {
        int code;
        if (e instanceof BusinessException) {
            code = ((BusinessException) e).getStatus().code();
        } else if (e.getCause() instanceof BusinessException) {
            code = ((BusinessException) e.getCause()).getStatus().code();
        } else {
            code = getStatus(request).value();
        }
        return logResponse(new JsonResult(code, e.getMessage(), null), e, request);
    }

    private JsonResult logResponse(JsonResult response, Exception e, HttpServletRequest request) {
        log.warn("Rest-URI: " + request.getRequestURI() + ", Exception: " + e.getMessage(), e);
        return response;
    }

    /**
     * 获取状态码
     *
     * @param request
     * @return
     */
    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
