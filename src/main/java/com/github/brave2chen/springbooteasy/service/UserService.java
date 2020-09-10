package com.github.brave2chen.springbooteasy.service;

import com.diboot.core.binding.Binder;
import com.github.brave2chen.springbooteasy.core.BaseServiceImpl;
import com.github.brave2chen.springbooteasy.entity.User;
import com.github.brave2chen.springbooteasy.mapper.UserMapper;
import com.github.brave2chen.springbooteasy.dto.UserWithAuth;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户 Service
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
@Service
public class UserService extends BaseServiceImpl<UserMapper, User> {
    public List<UserWithAuth> getUserVO(List<? extends User> users) {
        List<UserWithAuth> list = Binder.convertAndBindRelations(users, UserWithAuth.class);
        return list;
    }

    public UserWithAuth getUserVO(User user) {
        return Binder.convertAndBindRelations(user, UserWithAuth.class);
    }
}
