package com.github.brave2chen.springbooteasy.config.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.brave2chen.springbooteasy.config.filter.SetMDCUserFilter;
import com.github.brave2chen.springbooteasy.model.User;
import com.github.brave2chen.springbooteasy.service.AuthorityService;
import com.github.brave2chen.springbooteasy.service.RoleService;
import com.github.brave2chen.springbooteasy.service.UserService;
import com.github.brave2chen.springbooteasy.vo.UserVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author brave2chen
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private AuthorityService authorityService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
            if (user == null) {
                throw new UsernameNotFoundException(username + "用户不存在");
            }
            UserVO userVO = userService.getUserVO(user);
            userVO.setRoles(roleService.getRoleVO(userVO.getRoles()));
            return SecurityUser.of(userVO);
        };
    }

    @Bean
    public FilterInvocationSecurityMetadataSource mySecurityMetadataSource(FilterInvocationSecurityMetadataSource parent) {
        return new FilterInvocationSecurityMetadataSource() {
            private final AntPathMatcher antPathMatcher = new AntPathMatcher();
            private final Map<String, String> urlRoleMap = new HashMap<>(16);

            private void init() {
                // TODO 需要同步刷新
                authorityService.list().stream().forEach(authority -> {
                    urlRoleMap.put(authority.getMethod() + authority.getPath(), authority.getMethod() + authority.getPath());
                });
            }

            @Override
            public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
                if (urlRoleMap.isEmpty()) {
                    this.init();
                }
                Collection<ConfigAttribute> attributes = new ArrayList<>();
                FilterInvocation fi = (FilterInvocation) object;
                String url = fi.getHttpRequest().getMethod() + fi.getRequestUrl();
                for (Map.Entry<String, String> entry : urlRoleMap.entrySet()) {
                    if (antPathMatcher.match(entry.getKey(), url)) {
                        attributes.addAll(SecurityConfig.createList(entry.getValue()));
                        break;
                    }
                }
                // 返回 parent 配置的 attributes
                if (attributes.isEmpty() && parent.getAttributes(object) != null) {
                    attributes.addAll(parent.getAttributes(object));
                }
                return attributes;
            }

            @Override
            public Collection<ConfigAttribute> getAllConfigAttributes() {
                return null;
            }

            @Override
            public boolean supports(Class<?> clazz) {
                return FilterInvocation.class.isAssignableFrom(clazz);
            }
        };
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

    @Bean
    public MyAccessDeniedHandler myMyAccessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Bean
    public MyAuthenticationEntryPoint myAuthenticationEntryPoint() {
        return new MyAuthenticationEntryPoint();
    }

    @Bean
    public MyAuthenticationFailureHandler myAuthenticationFailureHandler() {
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
            .addFilterAfter(new SetMDCUserFilter(), AnonymousAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint())
                .accessDeniedHandler(myMyAccessDeniedHandler())
                .and()
            .formLogin()
                .successHandler(myAuthenticationSuccessHandler())
                .failureHandler(myAuthenticationFailureHandler())
                .permitAll()
                .and()
            .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler())
                .permitAll()
                .and()
            .httpBasic()
                .and()
            .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        fsi.setSecurityMetadataSource(mySecurityMetadataSource(fsi.getSecurityMetadataSource()));
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
        web.ignoring().antMatchers("/**/*.*", "/swagger-resources/**", "/v2/**", "/error");
    }


}
