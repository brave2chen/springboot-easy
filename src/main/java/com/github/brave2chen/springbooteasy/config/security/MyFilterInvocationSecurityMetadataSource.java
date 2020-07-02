package com.github.brave2chen.springbooteasy.config.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenqy28
 */
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private FilterInvocationSecurityMetadataSource parent;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final Map<String, String> urlRoleMap = new HashMap<String, String>() {{
        // TODO 应该从DB获取并缓存，当权限变更实时进行缓存更新
        put("/dictionary/**", AuthenticatedVoter.IS_AUTHENTICATED_FULLY);
        put("*/code/**", "ROLE_ADMIN");
        put("GET/flywaySchemaHistory/**", "ROLE_ANONYMOUS");
    }};

    public MyFilterInvocationSecurityMetadataSource(FilterInvocationSecurityMetadataSource parent) {
        this.parent = parent;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
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
        if (parent.getAttributes(object) != null) {
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