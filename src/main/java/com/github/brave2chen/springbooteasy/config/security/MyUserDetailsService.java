package com.github.brave2chen.springbooteasy.config.security;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.brave2chen.springbooteasy.dto.RoleWithAuth;
import com.github.brave2chen.springbooteasy.dto.UserWithAuth;
import com.github.brave2chen.springbooteasy.entity.User;
import com.github.brave2chen.springbooteasy.service.RoleService;
import com.github.brave2chen.springbooteasy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * MyUserDetailsService
 *
 * @author brave2chen
 * @date 2020-10-09
 */
@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {
    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (Validator.isMobile(username)) {
            user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, username));
        } else if (Validator.isEmail(username)) {
            user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, username));
        } else if (Validator.isGeneral(username)) {
            user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        } else {
            throw new AuthenticationServiceException(username + "格式不正确");
        }
        if (user == null) {
            throw new UsernameNotFoundException(username + "用户不存在");
        }
        UserWithAuth userWithAuth = userService.convertToViewObject(user, UserWithAuth.class);
        userWithAuth.setRoles(roleService.convertToViewObjectList(userWithAuth.getRoles(), RoleWithAuth.class));
        return SecurityUser.of(userWithAuth);
    }
}
