package com.github.brave2chen.springbooteasy.entity;

import com.github.brave2chen.springbooteasy.core.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色权限关系
 * </p>
 *
 * @author brave2chen
 * @since 2020-09-15
 */
@Data
@Accessors(chain = true)
@ApiModel(value="RoleAuthority对象", description="角色权限关系")
public class RoleAuthority implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty(value = "权限ID")
    private Integer authorityId;


}
