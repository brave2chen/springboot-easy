package com.github.brave2chen.springbooteasy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diboot.core.binding.Binder;
import com.github.brave2chen.springbooteasy.mapper.UserMapper;
import com.github.brave2chen.springbooteasy.entity.User;
import com.github.brave2chen.springbooteasy.query.UserQuery;
import com.github.brave2chen.springbooteasy.vo.UserVO;
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
public class UserService extends ServiceImpl<UserMapper, User> {
    public Page<User> page(UserQuery query) {
        Page<User> page = this.page(query.page(), query.wrapper().orderByDesc(User::getId));
        return page;
    }

    public List<UserVO> getUserVO(List<? extends User> users) {
        List<UserVO> list = Binder.convertAndBindRelations(users, UserVO.class);
        return list;
    }

    public UserVO getUserVO(User user) {
        return Binder.convertAndBindRelations(user, UserVO.class);
    }
}
