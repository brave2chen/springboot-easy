package com.github.brave2chen.springbooteasy.config.security;

import com.diboot.core.config.Cons;
import com.diboot.core.vo.JsonResult;
import com.github.brave2chen.springbooteasy.entity.Role;
import com.github.brave2chen.springbooteasy.service.UserService;
import com.github.brave2chen.springbooteasy.util.JsonUtil;
import com.github.brave2chen.springbooteasy.dto.UserWithAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author brave2chen
 */
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Login: " + authentication.getName());
        response.setCharacterEncoding(Cons.CHARSET_UTF8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
        SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setToken(Base64Utils.encodeToString((username + ":" + password).getBytes()));
        UserWithAuth userWithAuth = user.getUser();
        response.getWriter().print(JsonUtil.stringify((JsonResult.OK(new HashMap(2) {{
            put("token", user.getToken());
            put("user", username);
            put("roles", userWithAuth.getRoles().stream().map(Role::getCode).collect(Collectors.toList()));
        }}))));
    }
}
