package com.github.brave2chen.springbooteasy.config.security.email;

import com.github.brave2chen.springbooteasy.config.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.stereotype.Component;

/**
 * @author brave2chen
 */
@Component
public class EmailAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private EmailCodeGeneratingFilter emailCodeGeneratingFilter = new EmailCodeGeneratingFilter();

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        EmailAuthenticationFilter emailAuthenticationFilter = new EmailAuthenticationFilter();
        emailAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        emailAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        emailAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        EmailAuthenticationProvider emailAuthenticationProvider = new EmailAuthenticationProvider(userDetailsService);

        http.authenticationProvider(emailAuthenticationProvider)
                .addFilterAfter(emailAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterAfter(emailCodeGeneratingFilter, DefaultLoginPageGeneratingFilter.class);
    }
}
