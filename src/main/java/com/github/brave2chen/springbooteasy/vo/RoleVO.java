package com.github.brave2chen.springbooteasy.vo;

import com.diboot.core.binding.annotation.BindEntityList;
import com.github.brave2chen.springbooteasy.entity.Authority;
import com.github.brave2chen.springbooteasy.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户 VO
 *
 * @author brave2chen
 * @date 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "用户VO对象", description = "用户VO对象")
public class RoleVO extends Role {

    @ApiModelProperty(value = "用户角色")
    @BindEntityList(entity = Authority.class, condition="this.id=role_authority.role_id AND role_authority.authority_id=id")
    List<Authority> authorities;
}
