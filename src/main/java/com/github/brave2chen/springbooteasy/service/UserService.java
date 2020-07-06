package com.github.brave2chen.springbooteasy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.brave2chen.springbooteasy.mapper.UserMapper;
import com.github.brave2chen.springbooteasy.model.User;
import com.github.brave2chen.springbooteasy.query.UserQuery;
import com.github.brave2chen.springbooteasy.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户 Service
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Resource
    private RoleService roleService;

    public Page<User> page(UserQuery query) {
        Page<User> page = this.page(query.page(), query.wrapper().orderByDesc(User::getId));
        return page;
    }

    public List<UserVO> getUserVO(List<? extends User> users) {
        return users.stream().map(user -> this.getUserVO(user)).collect(Collectors.toList());
    }

    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        return UserVO.of(user).setRoles(roleService.selectByUserId(user.getId()));
    }
}