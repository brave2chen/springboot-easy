-- 预设用户信息
INSERT INTO user (id, username, mobile, email, nickname, gender, password, password_update_time, is_enabled, is_locked, is_expired, is_password_expired, is_deleted, create_user, create_time, update_user, update_time)
VALUES (1, 'admin', '13512345678', 'admin@foxmail.com', '预设管理员', 0, '$2a$10$PRvjCDPZ.kKd4OeORzEEMenDaGNaUxCi4EimY9o.D7oDlYnbC.Z2S', '2020-08-14 09:40:42', 1, 0, 0, 0, 0, 0, '2020-08-14 09:40:42', 0, '2020-08-14 09:40:42');
INSERT INTO user (id, username, mobile, email, nickname, gender, password, password_update_time, is_enabled, is_locked, is_expired, is_password_expired, is_deleted, create_user, create_time, update_user, update_time)
VALUES (2, 'user', '13612345678', 'user@foxmail.com', '预设用户', 0, '$2a$10$PRvjCDPZ.kKd4OeORzEEMenDaGNaUxCi4EimY9o.D7oDlYnbC.Z2S', '2020-08-14 09:40:42', 1, 0, 0, 0, 0, 0, '2020-08-14 09:40:42', 0, '2020-08-14 09:40:42');

-- 预设角色信息
INSERT INTO role (id, name, code, is_deleted, create_user, create_time) VALUES (1, '超级管理员', 'SystemAdmin', 0, 1, '2020-08-14 09:48:57');
INSERT INTO role (id, name, code, is_deleted, create_user, create_time) VALUES (2, '普通用户', 'User', 0, 1, '2020-08-14 09:48:57');

-- 用户-角色关系
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);

-- 资源权限定义
INSERT INTO authority (id, name, method, path, is_deleted, create_user, create_time) VALUES (1, '用户管理', '', '/user/**', 0, 1, '2020-08-14 09:52:15');
INSERT INTO authority (id, name, method, path, is_deleted, create_user, create_time) VALUES (2, '角色管理', '', '/role/**', 0, 1, '2020-08-14 09:52:15');
INSERT INTO authority (id, name, method, path, is_deleted, create_user, create_time) VALUES (3, '权限管理', '', '/authority/**', 0, 1, '2020-08-14 09:52:15');
INSERT INTO authority (id, name, method, path, is_deleted, create_user, create_time) VALUES (4, '字典管理', '', '/dictionary/**', 0, 1, '2020-08-14 09:52:15');
INSERT INTO authority (id, name, method, path, is_deleted, create_user, create_time) VALUES (5, '查询权限', 'GET', '/**', 0, 1, '2020-08-14 09:52:15');

-- 角色-资源权限关系
INSERT INTO role_authority (role_id, authority_id) VALUES (2, 5);

-- 字典：性别
INSERT INTO dictionary (id, parent_id, type, item_name, item_value, description, extdata, sort_id, is_editable, is_deletable, is_deleted, create_time) VALUES (1, 0, 'sex', '性别', '0', '', null, 1, 1, 0, 0, '2020-08-14 10:33:22');
INSERT INTO dictionary (id, parent_id, type, item_name, item_value, description, extdata, sort_id, is_editable, is_deletable, is_deleted, create_time) VALUES (2, 1, 'sex', '保密', '0', '', null, 1, 1, 0, 0, '2020-08-14 10:33:22');
INSERT INTO dictionary (id, parent_id, type, item_name, item_value, description, extdata, sort_id, is_editable, is_deletable, is_deleted, create_time) VALUES (3, 1, 'sex', '男', '1', '', null, 2, 1, 0, 0, '2020-08-14 10:33:22');
INSERT INTO dictionary (id, parent_id, type, item_name, item_value, description, extdata, sort_id, is_editable, is_deletable, is_deleted, create_time) VALUES (4, 1, 'sex', '女', '2', '', null, 3, 1, 0, 0, '2020-08-14 10:33:22');
