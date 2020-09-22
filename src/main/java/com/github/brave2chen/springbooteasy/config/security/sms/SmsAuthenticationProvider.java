package com.github.brave2chen.springbooteasy.config.security.sms;

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
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    /**
     * 身份逻辑验证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;

        this.checkSmsCode(authenticationToken);

        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());

        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(user, null, user.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    private void checkSmsCode(SmsAuthenticationToken authenticationToken) {
        Object principal = authenticationToken.getPrincipal();
        Object credentials = authenticationToken.getCredentials();
        Assert.notNull(principal, "手机号不能为空");
        Assert.notNull(credentials, "验证码为空");
        String mobile = principal.toString();
        String code = credentials.toString();
        Assert.hasText(mobile, "手机号不能为空");
        Assert.hasText(code, "验证码为空");

        if (!SmsCodeUtil.validateCode(mobile, code)) {
            throw new AuthenticationServiceException("验证码错误");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
