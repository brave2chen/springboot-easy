package com.github.brave2chen.springbooteasy.config.security;

import com.diboot.core.binding.Binder;
import com.github.brave2chen.springbooteasy.vo.RoleVO;
import com.github.brave2chen.springbooteasy.vo.UserVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SecurityUser
 *
 * @author brave2chen
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
        if (this.user == null || CollectionUtils.isEmpty(this.user.getRoles())) {
            return authorities;
        }
        this.user.setRoles(Binder.convertAndBindRelations(this.user.getRoles(), RoleVO.class));

        this.user.getRoles().stream().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
            if (!CollectionUtils.isEmpty(role.getAuthorities())) {
                List<GrantedAuthority> list = role.getAuthorities().stream().map(
                        authority -> new SimpleGrantedAuthority(authority.getAuthority())
                ).collect(Collectors.toList());
                authorities.addAll(list);
            }
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

    public Long getId() {
        return this.user.getId();
    }

    public static SecurityUser of(UserVO user) {
        return new SecurityUser(user);
    }

}
