package com.github.brave2chen.springbooteasy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    @ApiModelProperty(value = "删除标记", hidden = true)
    @TableField("is_deleted")
    @TableLogic
    @JsonIgnore
    private Boolean deleted;

    @ApiModelProperty(value = "创建用户")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新用户")
    private Long updateUser;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
