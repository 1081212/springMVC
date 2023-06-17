package org.example.gateway.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.gateway.entity.User;

import java.util.List;


@Mapper
public interface UserMapper {
    User getUserById(Integer id);

    List<User> getAllUser();

    User getUserByUsername(String username);

    int insertUser(User user);
}
