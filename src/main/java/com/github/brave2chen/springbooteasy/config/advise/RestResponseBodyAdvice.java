package com.github.brave2chen.springbooteasy.config.advise;

import com.github.brave2chen.springbooteasy.SpringBootEasyApplication;
import com.github.brave2chen.springbooteasy.core.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * RestResponseBodyAdvice
 *
 * @author chenqy28
 * @date 2020-05-29
 */
@ControllerAdvice
@Slf4j
public class RestResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 本项目包路径下Controller的请求才进行拦截
        return returnType.getExecutable().getDeclaringClass().getName().startsWith(SpringBootEasyApplication.class.getPackage().getName());
    }

    @Override
    public Object beforeBodyWrite(
            Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response
    ) {
        String path = request.getURI().getPath();
        body = body instanceof RestResponse ? body : RestResponse.ok(body);
        return body;
    }
}