package org.example.userserver.service;

import org.example.userserver.entity.User;
import org.example.userserver.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserMapper{

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAllUser(){
        return userMapper.getAllUser();
    }

    @Override
    public List<User> getUsersByPage(int start, int pageSize) {
        return userMapper.getUsersByPage(start,pageSize);
    }

    @Override
    public int countAllUsers() {
        return userMapper.countAllUsers();
    }
}
