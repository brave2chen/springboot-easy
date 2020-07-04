package com.github.brave2chen.springbooteasy.config.security;

import com.github.brave2chen.springbooteasy.vo.RoleVO;
import com.github.brave2chen.springbooteasy.vo.UserVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SecurityUser
 *
 * @author chenqy28
 * @date 2020-07-04
 */
public class SecurityUser implements UserDetails {
    private UserVO user;

    private SecurityUser(UserVO user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        this.user.getRoles().stream().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
            List<GrantedAuthority> collect = ((RoleVO) role).getAuthorities().stream().map(
                    authority -> new SimpleGrantedAuthority(authority.getMethod() + authority.getPath())
            ).collect(Collectors.toList());
            authorities.addAll(collect);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.user.getExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.user.getPasswordExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.getEnabled();
    }

    public static SecurityUser of(UserVO user) {
        return new SecurityUser(user);
    }

}
