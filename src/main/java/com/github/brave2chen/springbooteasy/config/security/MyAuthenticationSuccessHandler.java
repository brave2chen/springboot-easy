package com.github.brave2chen.springbooteasy.config.security;

import com.diboot.core.util.BeanUtils;
import com.github.brave2chen.springbooteasy.core.RestResponse;
import com.github.brave2chen.springbooteasy.model.User;
import com.github.brave2chen.springbooteasy.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Base64Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author brave2chen
 */
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Login: " + authentication.getName());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Object principal = authentication.getPrincipal();
        String password = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
        String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
        response.getWriter().print(JsonUtil.stringify((RestResponse.ok("登录成功", new HashMap(2) {{
            put("token", Base64Utils.encodeToString((username + ":" + password).getBytes()));
            put("user", BeanUtils.convert(principal, User.class));
        }}))));
    }
}
