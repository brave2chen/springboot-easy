package com.github.brave2chen.springbooteasy.config.security;

import com.github.brave2chen.springbooteasy.service.AuthorityService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * MySecurityMetadataSource
 *
 * @author brave2chen
 * @date 2020-09-17
 */
@Component
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    /**
     * 权限缓存
     */
    private static final Map<AntPathRequestMatcher, String> urlRoleMap = new HashMap<>(16);

    private AuthorityService authorityService;

    private FilterInvocationSecurityMetadataSource parent;

    public void setParent(FilterInvocationSecurityMetadataSource parent) {
        this.parent = parent;
    }

    public MySecurityMetadataSource(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    private void init() {
        authorityService.list().stream().forEach(authority -> {
            urlRoleMap.put(new AntPathRequestMatcher(authority.getPath(), authority.getMethod()), authority.getAuthority());
        });
    }

    public void refresh() {
        urlRoleMap.clear();
        this.init();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (urlRoleMap.isEmpty()) {
            this.init();
        }
        Collection<ConfigAttribute> attributes = new ArrayList<>();
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        for (Map.Entry<AntPathRequestMatcher, String> entry : urlRoleMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                attributes.addAll(SecurityConfig.createList(entry.getValue()));
            }
        }
        // 返回代码定义的默认配置
        if (attributes.isEmpty() && parent != null && parent.getAttributes(object) != null) {
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
}
