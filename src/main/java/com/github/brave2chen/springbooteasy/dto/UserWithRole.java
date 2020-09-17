package com.github.brave2chen.springbooteasy.dto;

import com.diboot.core.binding.annotation.BindEntityList;
import com.diboot.core.binding.annotation.BindFieldList;
import com.github.brave2chen.springbooteasy.entity.Role;
import com.github.brave2chen.springbooteasy.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author Administrator
 * @date 2020-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "用户信息对象", description = "用户信息对象")
public class UserWithRole extends User {

    @ApiModelProperty(value = "用户角色")
    @BindEntityList(entity = Role.class, condition = "this.id=user_role.user_id AND user_role.role_id=id")
    private List<Role> roles;

    public static UserWithRole from(UserWithAuth userWithAuth) {
        UserWithRole userWithRole = new UserWithRole();
        BeanUtils.copyProperties(userWithAuth, userWithRole);
        List<Role> roles = userWithAuth.getRoles().stream().map(role -> {
            Role role1 = new Role();
            BeanUtils.copyProperties(role, role1);
            return role1;
        }).collect(Collectors.toList());
        userWithRole.setRoles(roles);
        return userWithRole;
    }

}
