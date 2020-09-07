package com.github.brave2chen.springbooteasy.config.security;

import com.diboot.core.vo.JsonResult;
import com.github.brave2chen.springbooteasy.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author brave2chen
 */
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String msg;
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            msg = "用户名或密码错误";
        } else if (e instanceof DisabledException) {
            msg = "用户已被禁用";
        } else if (e instanceof LockedException) {
            msg = "账户被锁定";
        } else if (e instanceof AccountExpiredException) {
            msg = "账户过期";
        } else if (e instanceof CredentialsExpiredException) {
            msg = "证书过期";
        } else {
            msg = e.getMessage();
        }
        log.error("Login Error, Exception: " + e.getMessage(), e);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(JsonUtil.stringify((JsonResult.FAIL_OPERATION(msg))));
    }
}
