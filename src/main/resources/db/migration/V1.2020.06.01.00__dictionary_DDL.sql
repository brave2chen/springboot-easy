-- 建表
create table dictionary
(
    id          int unsigned not null auto_increment comment 'id' primary key,
    parent_id   int unsigned          default '0' not null comment '父id',
    type        varchar(50)  not null comment '字典类型',
    item_name   varchar(100) not null comment '显示名',
    item_value  varchar(100)          default null comment '存储值',
    description varchar(100)          default null comment '描述说明',
    `index`     smallint     not null default '99' comment '排序号',
    is_deleted  tinyint(1)   not null default '0' comment '删除标记',
    create_user bigint unsigned       default 0 not null comment '创建用户',
    create_time timestamp    not null default current_timestamp comment '创建时间',
    index idx_directory (type, item_value)
) comment '数据字典';