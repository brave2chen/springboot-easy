package com.github.brave2chen.springbooteasy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.brave2chen.springbooteasy.mapper.RoleMapper;
import com.github.brave2chen.springbooteasy.model.Role;
import com.github.brave2chen.springbooteasy.query.RoleQuery;
import com.github.brave2chen.springbooteasy.vo.RoleVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AuthorityService authorityService;

    /**
     * 根据用户ID，查询全部记录
     *
     * @param userId
     */
    public List<Role> selectByUserId(Long userId) {
        return roleMapper.selectByUserId(userId);
    }

    public Page<Role> page(RoleQuery query) {
        Page<Role> page = this.page(query.page(), query.wrapper().orderByDesc(Role::getId));
        return page;
    }

    public List<RoleVO> getRoleVO(List<? extends Role> roles) {
        return roles.stream().map(role -> this.getRoleVO(role)).collect(Collectors.toList());
    }

    public RoleVO getRoleVO(Role role) {
        if (role == null) {
            return null;
        }
        return RoleVO.of(role).setAuthorities(authorityService.selectByUserId(role.getId()));
    }
}