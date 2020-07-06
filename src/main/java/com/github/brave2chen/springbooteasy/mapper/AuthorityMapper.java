package com.github.brave2chen.springbooteasy.mapper;

import com.github.brave2chen.springbooteasy.model.Authority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.brave2chen.springbooteasy.model.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
public interface AuthorityMapper extends BaseMapper<Authority> {
    /**
     * 根据角色ID，查询全部记录
     *
     * @param roleId
     */
    @Select("select authority.* from authority, role_authority where authority.id = role_authority.authority_id and role_authority.role_id = #{roleId}")
    List<Authority> selectByUserId(Long roleId);
}
