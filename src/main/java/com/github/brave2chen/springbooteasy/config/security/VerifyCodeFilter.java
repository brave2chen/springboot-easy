package com.github.brave2chen.springbooteasy.config.security;

import cn.hutool.core.lang.UUID;
import com.diboot.core.config.Cons;
import com.diboot.core.vo.JsonResult;
import com.diboot.core.vo.KeyValue;
import com.github.brave2chen.springbooteasy.util.JsonUtil;
import com.github.brave2chen.springbooteasy.util.RedisUtil;
import com.wf.captcha.SpecCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录图片验证码 过滤器
 *
 * @author chenqy28
 */
@Component
@Slf4j
public class VerifyCodeFilter extends OncePerRequestFilter {
    public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";

    private RequestMatcher loginMatcher = new AntPathRequestMatcher("/login", "POST");
    private RequestMatcher getCodeMatcher = new AntPathRequestMatcher("/login", "GET");

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取验证码
        if (getCodeMatcher.matches(request)) {
            KeyValue keyValue = VerifyCodeUtil.getCodeBase64Image();
            response.setCharacterEncoding(Cons.CHARSET_UTF8);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().print(JsonUtil.stringify(JsonResult.OK(keyValue)));
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        // 进行验证码的校验
        if (loginMatcher.matches(request)) {
            try {
                String code = request.getParameter(SPRING_SECURITY_FORM_CODE_KEY);

                if (StringUtils.isBlank(code)) {
                    throw new AuthenticationServiceException("验证码为空");
                }

                if (!code.contains(":")) {
                    throw new AuthenticationServiceException("验证码格式不合法");
                }
                String[] split = code.split(":");
                if (split.length != 2) {
                    throw new AuthenticationServiceException("验证码格式不合法");
                }
                String key = split[0];
                String verCode = split[1];

                if (!VerifyCodeUtil.validateCode(key, verCode)) {
                    throw new AuthenticationServiceException("验证码不正确！");
                }
            } catch (AuthenticationException e) {
                // 2. 捕获步骤1中校验出现异常，交给失败处理类进行进行处理
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
            } finally {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }


    }

    /**
     * VerifyCodeUtil
     *
     * @author brave2chen
     * @date 2020-09-21
     */
    public static class VerifyCodeUtil {
        private static final String VERIFY_CODE_KEY = "VERIFY_CODE_KEY";

        /**
         * 验证码有效期：30秒
         */
        private static final long SMS_CODE_EXPIRE_TIME = 30;

        /**
         * 生成验证码
         *
         * @return
         */
        public static KeyValue getCodeBase64Image() {
            String key = UUID.randomUUID().toString(true);

            SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
            String code = specCaptcha.text().toLowerCase();
            String image = specCaptcha.toBase64();
            log.debug("生成图片验证码，key: {}, code: {}", key, code);
            RedisUtil.hset(VERIFY_CODE_KEY, key, code, SMS_CODE_EXPIRE_TIME);

            return new KeyValue(key, image);
        }

        /**
         * 校验code的有效性
         *
         * @param key
         * @param code
         * @return
         */
        public static boolean validateCode(String key, String code) {
            Assert.hasText(key, "key不能为空");
            Assert.hasText(code, "验证码不能为空");
            String realCode = RedisUtil.hget(VERIFY_CODE_KEY, key);
            log.debug("开始校验验证码，key: {}, code: {}, realCode: {}", key, code, realCode);
            return code.equals(realCode);
        }
    }

}
