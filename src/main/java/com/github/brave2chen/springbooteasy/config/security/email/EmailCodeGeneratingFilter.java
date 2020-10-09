package com.github.brave2chen.springbooteasy.config.security.email;

import com.diboot.core.config.Cons;
import com.diboot.core.vo.JsonResult;
import com.github.brave2chen.springbooteasy.config.security.email.EmailAuthenticationFilter;
import com.github.brave2chen.springbooteasy.config.security.email.EmailCodeUtil;
import com.github.brave2chen.springbooteasy.util.JsonUtil;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * EmailGenerateCodeFilter
 *
 * @author brave2chen
 * @date 2020-09-21
 */
public class EmailCodeGeneratingFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (isLoginUrlRequest(request)) {
            String email = obtainMobile(request);

            // TODO 需要验证用户信息，获取真实email
            EmailCodeUtil.sendCode(email);

            response.setCharacterEncoding(Cons.CHARSET_UTF8);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().print(JsonUtil.stringify(JsonResult.OK()));
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isLoginUrlRequest(HttpServletRequest request) {
        return matches(request, EmailAuthenticationFilter.EMAIL_LOGIN_URL);
    }

    private boolean matches(HttpServletRequest request, String url) {
        if (!"GET".equals(request.getMethod()) || url == null) {
            return false;
        }
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');

        if (pathParamIndex > 0) {
            // strip everything after the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }

        return uri.equals(request.getContextPath() + url);
    }

    @Nullable
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(EmailAuthenticationFilter.SPRING_SECURITY_FORM_MOBILE_KEY);
    }
}
