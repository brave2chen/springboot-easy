package com.github.brave2chen.springbooteasy.config.security;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.brave2chen.springbooteasy.config.filter.SetMDCUserFilter;
import com.github.brave2chen.springbooteasy.config.security.email.EmailAuthenticationFilter;
import com.github.brave2chen.springbooteasy.config.security.email.EmailAuthenticationSecurityConfig;
import com.github.brave2chen.springbooteasy.config.security.jwt.JwtTokenAuthenticationFilter;
import com.github.brave2chen.springbooteasy.config.security.sms.SmsAuthenticationFilter;
import com.github.brave2chen.springbooteasy.config.security.sms.SmsAuthenticationSecurityConfig;
import com.github.brave2chen.springbooteasy.dto.RoleWithAuth;
import com.github.brave2chen.springbooteasy.dto.UserWithAuth;
import com.github.brave2chen.springbooteasy.entity.User;
import com.github.brave2chen.springbooteasy.service.AuthorityService;
import com.github.brave2chen.springbooteasy.service.RoleService;
import com.github.brave2chen.springbooteasy.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author brave2chen
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private MySecurityMetadataSource securityMetadataSource;

    @Resource
    private MyAccessDeniedHandler accessDeniedHandler;

    @Resource
    private MyAuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    @Resource
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Resource
    private MyLogoutSuccessHandler logoutSuccessHandler;

    @Resource
    private MyUserDetailsService userDetailsService;

    @Resource
    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDecisionManager myAccessDecisionManager() {
        return new AffirmativeBased(Arrays.asList(
                new WebExpressionVoter(),
                new AuthenticatedVoter(),
                new RoleVoter(),
                new PrivilegeVoter()
        ));
    }

    /**
     * security CorsConfigurer 会自动注入这个Bean
     */
    @Bean(name = "corsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader(HttpHeaders.AUTHORIZATION);
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Resource
    private SmsAuthenticationSecurityConfig smsAuthenticationSecurityConfig;

    @Resource
    private EmailAuthenticationSecurityConfig emailAuthenticationSecurityConfig;

    @Resource
    private VerifyCodeFilter verifyCodeFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource()).and()
            .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new SetMDCUserFilter(), AnonymousAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
            .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
            .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
                .and()
            .httpBasic()
                .and()
            .apply(smsAuthenticationSecurityConfig)
                .and()
                .authorizeRequests()
                .antMatchers(SmsAuthenticationFilter.SMS_LOGIN_URL).permitAll()
                .and()
            .apply(emailAuthenticationSecurityConfig)
                .and()
                .authorizeRequests()
                .antMatchers(EmailAuthenticationFilter.EMAIL_LOGIN_URL).permitAll()
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        securityMetadataSource.setParent(fsi.getSecurityMetadataSource());
                        fsi.setSecurityMetadataSource(securityMetadataSource);
                        fsi.setAccessDecisionManager(myAccessDecisionManager());
                        return fsi;
                    }
                })
                // 所有请求登录才能访问
                .anyRequest().authenticated()
                .and()

        ;
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 放开静态资源访问、swagger接口访问
        web.ignoring().antMatchers("/**/*.*", "/swagger-resources/**", "/v2/**", "/error", "/druid/**");
    }
}
