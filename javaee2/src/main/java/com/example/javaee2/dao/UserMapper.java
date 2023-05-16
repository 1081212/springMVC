package com.example.javaee2.dao;

import com.example.javaee2.pojo.User;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


@Mapper
public interface UserMapper {
    User getUserById(Integer id);

    List<User> getAllUser();

    User getUserByUsername(String username);

    int insertUser(User user);
}
