package org.example.dao;

import org.example.pojo.User;

import java.util.List;

public interface UserMapper {
    User getUserById(Integer id);

    List<User> getAllUser();

    User getUserByUsername(String username);

    void insertUser(User user);
}
