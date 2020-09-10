package com.github.brave2chen.springbooteasy.service;

import com.diboot.core.binding.Binder;
import com.github.brave2chen.springbooteasy.core.BaseServiceImpl;
import com.github.brave2chen.springbooteasy.entity.Role;
import com.github.brave2chen.springbooteasy.mapper.RoleMapper;
import com.github.brave2chen.springbooteasy.dto.RoleWithAuth;
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
public class RoleService extends BaseServiceImpl<RoleMapper, Role> {

    public List<RoleWithAuth> getRoleVO(List<? extends Role> roles) {
        return Binder.convertAndBindRelations(roles, RoleWithAuth.class);
    }

    public RoleWithAuth getRoleVO(Role role) {
        return Binder.convertAndBindRelations(role, RoleWithAuth.class);
    }
}
