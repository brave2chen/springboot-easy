package com.github.brave2chen.springbooteasy.query;

import com.diboot.core.binding.query.BindQuery;
import com.github.brave2chen.springbooteasy.entity.Authority;
import com.github.brave2chen.springbooteasy.entity.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * User查询对象
 *
 * @author brave2chen
 * @date 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "数据字典 查询实体", description = "数据字典 查询实体")
public class AuthorityQuery extends Authority {
    @ApiModelProperty(value = "角色ID")
    @BindQuery(entity = Role.class, field = "id", condition = "this.id=role_authority.authority_id AND role_authority.role_id=id")
    private String roleId;
}
