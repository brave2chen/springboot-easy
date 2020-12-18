package com.github.brave2chen.springbooteasy.config.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author brave2chen
 */
public class PrivilegeVoter implements AccessDecisionVoter<Object> {
    /**
     * 系统管理员角色：放行所有请求
     */
    private static final String SYSTEM_ADMIN_ROLE = "SystemAdmin";

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return StringUtils.isNotBlank(attribute.getAttribute());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return ACCESS_DENIED;
        }
        if(true){
            return ACCESS_GRANTED;
        }
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);
        if (isSystemAdmin(authorities)) {
            return ACCESS_GRANTED;
        }

        int result = ACCESS_ABSTAIN;
        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;

                // Attempt to find a matching granted authority
                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }
        return result;
    }

    Collection<? extends GrantedAuthority> extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities();
    }

    boolean isSystemAdmin(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().anyMatch(authority -> SYSTEM_ADMIN_ROLE.equals(authority.getAuthority()));
    }
}
