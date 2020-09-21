package com.github.brave2chen.springbooteasy.config.security.sms;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.brave2chen.springbooteasy.config.security.SecurityUser;
import com.github.brave2chen.springbooteasy.dto.RoleWithAuth;
import com.github.brave2chen.springbooteasy.dto.UserWithAuth;
import com.github.brave2chen.springbooteasy.entity.User;
import com.github.brave2chen.springbooteasy.service.RoleService;
import com.github.brave2chen.springbooteasy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.stereotype.Component;

/**
 * @author chenqy28
 */
@Component
public class SmsAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private SmsCodeGeneratingFilter smsCodeGeneratingFilter = new SmsCodeGeneratingFilter();

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        smsAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
        smsAuthenticationProvider.setUserDetailsService((mobile) -> {
            User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, mobile));
            if (user == null) {
                throw new UsernameNotFoundException("手机号为" + mobile + "的用户不存在");
            }
            UserWithAuth userWithAuth = userService.convertToViewObject(user, UserWithAuth.class);
            userWithAuth.setRoles(roleService.convertToViewObjectList(userWithAuth.getRoles(), RoleWithAuth.class));
            return SecurityUser.of(userWithAuth);
        });

        http.authenticationProvider(smsAuthenticationProvider)
                .addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterAfter(smsCodeGeneratingFilter, DefaultLoginPageGeneratingFilter.class);
    }
}
