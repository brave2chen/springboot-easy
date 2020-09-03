package com.github.brave2chen.springbooteasy.entity;

import com.github.brave2chen.springbooteasy.core.LogicalEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Role对象", description = "角色")
public class Role extends LogicalEntity {

    private static final long serialVersionUID = 5066456485225243041L;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色代码")
    private String code;

}
