package com.github.brave2chen.springbooteasy.config.security;

import com.diboot.core.vo.JsonResult;
import com.github.brave2chen.springbooteasy.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author brave2chen
 */
@Slf4j
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error("Rest-URI: " + request.getRequestURI() + ", Exception: " + e.getMessage(), e);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(JsonUtil.stringify((JsonResult.FAIL_INVALID_TOKEN("用户未登录"))));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
