package com.github.brave2chen.springbooteasy.config.advise;

import com.github.brave2chen.springbooteasy.SpringBootEasyApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * RequestBodyAdvice
 *
 * @author chenqy28
 * @date 2020-05-29
 */
@ControllerAdvice
@Slf4j
public class RestRequestBodyAdvice extends RequestBodyAdviceAdapter {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 本项目包路径下Controller的请求才进行拦截
        return methodParameter.getExecutable().getDeclaringClass().getName().startsWith(SpringBootEasyApplication.class.getPackage().getName());
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
