package com.github.brave2chen.springbooteasy.query;

import com.diboot.core.binding.query.BindQuery;
import com.github.brave2chen.springbooteasy.entity.Role;
import com.github.brave2chen.springbooteasy.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * User 查询对象
 *
 * @author brave2chen
 * @date 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "角色 查询实体", description = "角色 查询实体")
public class RoleQuery extends Role {

    @ApiModelProperty(value = "用户ID")
    @BindQuery(entity = User.class, field = "id", condition = "this.id=user_role.role_id AND user_role.user_id=id")
    private String userId;
}
