package com.github.brave2chen.springbooteasy.query;

import com.diboot.core.binding.query.BindQuery;
import com.diboot.core.binding.query.Comparison;
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
@ApiModel(value = "User 查询实体", description = "User 查询实体")
public class UserQuery extends User {

    @ApiModelProperty(value = "性别")
    @BindQuery(comparison = Comparison.IN, field = "gender")
    private Integer[] sex;

    /**
     * 需要自行实现
     */
    @ApiModelProperty(value = "身份模糊匹配")
    @BindQuery(ignore = true)
    private String identity;

    @ApiModelProperty(value = "角色ID")
    @BindQuery(entity = Role.class, field = "id", condition = "this.id=user_role.user_id AND user_role.role_id=id")
    private String roleId;

//    FIXME 中间表关联条件暂只支持1张中间表
//    @ApiModelProperty(value = "权限ID")
//    @BindQuery(entity = Authority.class, field = "id", condition = "this.id=user_role.user_id AND user_role.role_id=role_authority.role_id AND role_authority.authority_id=id")
//    private String authorityId;
}
