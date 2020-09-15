package com.github.brave2chen.springbooteasy.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户角色关系
 * </p>
 *
 * @author brave2chen
 * @since 2020-09-15
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "UserRole对象", description = "用户角色关系")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;


}
