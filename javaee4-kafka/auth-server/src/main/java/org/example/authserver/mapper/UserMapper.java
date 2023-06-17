package org.example.authserver.mapper;

import org.example.authserver.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {
    User getUserById(Integer id);

    List<User> getAllUser();

    User getUserByUsername(String username);

    int insertUser(User user);
}
