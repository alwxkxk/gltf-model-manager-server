package com.alwxkxk.server.mapper;

import com.alwxkxk.server.dto.User;

import java.util.List;

public interface UserMapper {
    // 查询全部用户
    List<User> getUserList();

    // 根据id查询用户
    User getUserById(int id);

    //添加一个用户
    int addUser(User user);

    // 更新用户信息
    int updateUser(User user);

    // 删除用户
    int deleteUser(int id);
}
