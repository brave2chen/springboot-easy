package com.github.brave2chen.springbooteasy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.brave2chen.springbooteasy.core.LogicalDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author brave2chen
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "User对象", description = "用户")
public class User extends LogicalDataEntity {

    private static final long serialVersionUID = -969295658710451077L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户手机")
    private String mobile;

    @ApiModelProperty(value = "用户邮箱地址")
    private String email;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户性别：0保密,1男，2女")
    private Integer gender;

    @ApiModelProperty(value = "用户密码")
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "密码更新时间")
    private Date passwordUpdateTime;

    @ApiModelProperty(value = "是否启用")
    @TableField("is_enabled")
    private Boolean enabled;

    @ApiModelProperty(value = "是否锁定")
    @TableField("is_locked")
    private Boolean locked;

    @ApiModelProperty(value = "是否过期")
    @TableField("is_expired")
    private Boolean expired;

    @ApiModelProperty(value = "密码是否过期")
    @TableField("is_password_expired")
    private Boolean passwordExpired;
}
