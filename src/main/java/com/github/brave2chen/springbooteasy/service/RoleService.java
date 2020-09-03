package com.github.brave2chen.springbooteasy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diboot.core.binding.Binder;
import com.github.brave2chen.springbooteasy.mapper.RoleMapper;
import com.github.brave2chen.springbooteasy.entity.Role;
import com.github.brave2chen.springbooteasy.query.RoleQuery;
import com.github.brave2chen.springbooteasy.vo.RoleVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色 Service
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {
    public Page<Role> page(RoleQuery query) {
        Page<Role> page = this.page(query.page(), query.wrapper().orderByDesc(Role::getId));
        return page;
    }

    public List<RoleVO> getRoleVO(List<? extends Role> roles) {
        return Binder.convertAndBindRelations(roles, RoleVO.class);
    }

    public RoleVO getRoleVO(Role role) {
        return Binder.convertAndBindRelations(role, RoleVO.class);
    }
}
