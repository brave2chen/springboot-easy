package com.github.brave2chen.springbooteasy.vo;

import com.diboot.core.binding.annotation.BindDict;
import com.diboot.core.binding.annotation.BindFieldList;
import com.github.brave2chen.springbooteasy.dto.UserWithAuth;
import com.github.brave2chen.springbooteasy.entity.Role;
import com.github.brave2chen.springbooteasy.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

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
public class UserInfo extends User {
    @ApiModelProperty(value = "用户角色")
    @BindFieldList(entity = Role.class, field = "code", condition="this.id=user_role.user_id AND user_role.role_id=id")
    private List<String> roles;

    @ApiModelProperty(value = "用户性别")
    @BindDict(type = "sex", field = "gender")
    private String genderName;

    public static UserInfo from(UserWithAuth userWithAuth) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userWithAuth, userInfo);
        if (!CollectionUtils.isEmpty(userWithAuth.getRoles())) {
            userInfo.setRoles(userWithAuth.getRoles().stream().map(v -> v.getCode()).collect(Collectors.toList()));
        }
        return userInfo;
    }

}
