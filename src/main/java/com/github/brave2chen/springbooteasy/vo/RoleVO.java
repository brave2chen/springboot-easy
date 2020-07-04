package com.github.brave2chen.springbooteasy.vo;

import com.github.brave2chen.springbooteasy.model.Authority;
import com.github.brave2chen.springbooteasy.model.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 用户 VO
 *
 * @author chenqy28
 * @date 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "用户VO对象", description = "用户VO对象")
public class RoleVO extends Role {

    @ApiModelProperty(value = "用户角色")
    List<? extends Authority> authorities;

    public static RoleVO of(Role role) {
        RoleVO userVO = new RoleVO();
        BeanUtils.copyProperties(role, userVO);
        return userVO;
    }
}
