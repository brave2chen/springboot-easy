package com.github.brave2chen.springbooteasy.config.security.email;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @author brave2chen
 */
public class EmailAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private final Object principal;
    private Object credentials;

    public EmailAuthenticationToken(String email, String code) {
        super(null);
        this.principal = email;
        this.credentials = code;
        setAuthenticated(false);
    }


    public EmailAuthenticationToken(Object email, String code, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = email;
        this.credentials = code;
        super.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }


    @Override
    public Object getPrincipal() {
        return this.principal;
    }


    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }


    @Override


    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
