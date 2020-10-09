package com.github.brave2chen.springbooteasy.config.security.email;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

/**
 * @author brave2chen
 */
public class EmailAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public EmailAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 身份逻辑验证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        EmailAuthenticationToken authenticationToken = (EmailAuthenticationToken) authentication;

        this.checkEmailCode(authenticationToken);

        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());

        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        EmailAuthenticationToken authenticationResult = new EmailAuthenticationToken(user, null, user.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    private void checkEmailCode(EmailAuthenticationToken authenticationToken) {
        Object principal = authenticationToken.getPrincipal();
        Object credentials = authenticationToken.getCredentials();
        Assert.notNull(principal, "邮箱号不能为空");
        Assert.notNull(credentials, "验证码为空");
        String email = principal.toString();
        String code = credentials.toString();
        Assert.hasText(email, "邮箱号不能为空");
        Assert.hasText(code, "验证码为空");

        if (!EmailCodeUtil.validateCode(email, code)) {
            throw new AuthenticationServiceException("验证码错误");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
