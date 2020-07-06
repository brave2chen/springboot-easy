package com.github.brave2chen.springbooteasy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.brave2chen.springbooteasy.model.Role;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户ID，查询全部记录
     *
     * @param userId
     */
    @Select("select role.* from role, user_role where role.id = user_role.role_id and user_role.user_id = #{userId}")
    List<Role> selectByUserId(Long userId);
}
