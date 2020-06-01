package com.github.brave2chen.springbooteasy.config.advise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.brave2chen.springbooteasy.SpringBootEasyApplication;
import com.github.brave2chen.springbooteasy.core.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType().getName().startsWith(SpringBootEasyApplication.class.getPackage().getName());
    }

    @Override
    public Object beforeBodyWrite(
            Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response
    ) {
        String path = request.getURI().getPath();
        body = body instanceof RestResponse ? body : RestResponse.ok(body);
        try {
            log.info("Rest-URI: {}, Rest-RequestBody: {}", path, objectMapper.writeValueAsString(body));
        } catch (JsonProcessingException e) {
            log.error("Rest-URI: {}, Rest-RequestBody log 序列化异常: {}", path, e.getMessage());
        }
        return body;
    }
}