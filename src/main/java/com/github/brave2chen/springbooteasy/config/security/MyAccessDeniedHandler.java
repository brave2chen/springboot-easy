package com.github.brave2chen.springbooteasy.config.security;

import com.github.brave2chen.springbooteasy.core.RestResponse;
import com.github.brave2chen.springbooteasy.enums.ErrorCode;
import com.github.brave2chen.springbooteasy.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义AccessDeniedHandler
 *
 * @author brave2chen
 * @date 2020-06-22
 */
@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.error("Rest-URI: " + request.getRequestURI() + ", Exception: " + e.getMessage(), e);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(JsonUtil.stringify((RestResponse.fail(ErrorCode.A0301, e.getMessage()))));
    }
}