package com.github.brave2chen.springbooteasy.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author brave2chen
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // TODO UserDetailsService 改为数据库方式实现，权限需要试试刷新和缓存
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(new User("admin", passwordEncoder().encode("123456"), AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
        manager.createUser(new User("user", passwordEncoder().encode("123456"), AuthorityUtils.createAuthorityList("ROLE_USER")));
        return manager;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource mySecurityMetadataSource() {
        MyFilterInvocationSecurityMetadataSource securityMetadataSource = new MyFilterInvocationSecurityMetadataSource();
        return securityMetadataSource;
    }

    @Bean
    public AccessDecisionManager myAccessDecisionManager() {
        return new MyAccessDecisionManager();
    }

    @Bean
    public MyAccessDeniedHandler myMyAccessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Bean
    public MyAuthenticationEntryPoint myAuthenticationEntryPoint() {
        return new MyAuthenticationEntryPoint();
    }

    @Bean
    public MyAuthenticationFailureHandler MyAuthenticationFailureHandler() {
        return new MyAuthenticationFailureHandler();
    }

    @Bean
    public MyAuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new MyAuthenticationSuccessHandler();
    }

    @Bean
    public MyLogoutSuccessHandler myLogoutSuccessHandler() {
        return new MyLogoutSuccessHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint())
                .accessDeniedHandler(myMyAccessDeniedHandler())
                .and()
            .formLogin()
                .successHandler(myAuthenticationSuccessHandler())
                .failureHandler(MyAuthenticationFailureHandler())
                .permitAll()
                .and()
            .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler())
                .permitAll()
                .and()
            .httpBasic()
                .and()
            .authorizeRequests()
                .anyRequest().authenticated()
                .accessDecisionManager(myAccessDecisionManager())
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        fsi.setSecurityMetadataSource(mySecurityMetadataSource());
                        return fsi;
                    }
                })
        ;
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 放开静态资源访问、swagger接口访问
        web.ignoring().antMatchers("/**/*.*", "/swagger-resources/**", "/v2/**");
    }


}
