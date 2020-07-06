package com.github.brave2chen.springbooteasy.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.brave2chen.springbooteasy.mapper.AuthorityMapper;
import com.github.brave2chen.springbooteasy.model.Authority;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限 Service
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
@Service
public class AuthorityService extends ServiceImpl<AuthorityMapper, Authority> {
    @Resource
    private AuthorityMapper authorityMapper;

    /**
     * 根据用户ID，查询全部记录
     *
     * @param roleId
     */
    public List<Authority> selectByUserId(Long roleId) {
        return authorityMapper.selectByUserId(roleId);
    }
}