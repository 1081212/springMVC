package org.example.userserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.userserver.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> getAllUser();

    int countAllUsers();

    List<User> getUsersByPage( int start, int pageSize);

}
