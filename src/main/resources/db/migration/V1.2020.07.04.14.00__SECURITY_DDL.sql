create table user
(
    id                   bigint unsigned auto_increment comment '主键' primary key,
    username             varchar(20)                   not null comment '用户名',
    mobile               char(11)                      null comment '用户手机',
    email                varchar(50)                   null comment '用户邮箱地址',
    nickname             varchar(20)                   not null comment '用户昵称',
    gender               tinyint(2) unsigned default 0 not null comment '用户性别：0保密,1男，2女',
    password             varchar(255)                  not null comment '用户密码',
    password_update_time datetime                      not null comment '密码更新时间',
    is_enabled           tinyint(1) unsigned default 1 not null comment '是否启用',
    is_locked            tinyint(1) unsigned default 0 not null comment '是否锁定',
    is_expired           tinyint(1) unsigned default 0 not null comment '是否过期',
    is_password_expired  tinyint(1) unsigned default 0 not null comment '密码是否过期',
    is_deleted           tinyint(1) unsigned default 0 not null comment '是否删除',
    create_user          bigint unsigned     default 0 not null comment '创建用户',
    create_time          datetime                      not null comment '创建时间',
    update_user          bigint unsigned     default 0 not null comment '更新用户',
    update_time          datetime                      not null comment '更新时间',
    constraint uk_username unique (username),
    constraint uk_mobile unique (mobile),
    constraint uk_email unique (email)
) comment '用户';

create table role
(
    id          bigint unsigned auto_increment comment '主键' primary key,
    name        varchar(20)                   not null comment '角色名称',
    code        varchar(20)                   not null comment '角色代码',
    is_deleted  tinyint(1) unsigned default 0 not null comment '是否删除',
    create_user bigint unsigned     default 0 not null comment '创建用户',
    create_time datetime                      not null comment '创建时间',
    constraint uk_name unique (name),
    constraint uk_code unique (code)
) comment '角色';

create table user_role
(
    user_id int not null comment '用户ID',
    role_id int not null comment '角色ID',
    primary key (user_id, role_id),
    index idx_role_id (role_id)
) comment '用户角色关系';


create table authority
(
    id          bigint unsigned auto_increment comment '主键' primary key,
    name        varchar(20)                    not null comment '权限名称',
    method      varchar(20)         default '' not null comment '接口方法',
    path        varchar(20)                    not null comment '接口路径',
    is_deleted  tinyint(1) unsigned default 0  not null comment '是否删除',
    create_user bigint unsigned     default 0  not null comment '创建用户',
    create_time datetime                       not null comment '创建时间',
    constraint uk_name unique (name)
) comment '权限';

create table role_authority
(
    role_id      int not null comment '角色ID',
    authority_id int not null comment '权限ID',
    primary key (role_id, authority_id),
    index idx_authority_id (authority_id)
) comment '角色权限关系';


