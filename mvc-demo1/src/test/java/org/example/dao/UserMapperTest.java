package org.example.dao;

import org.example.pojo.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/springMVC.xml")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetUserById() {
        User user = userMapper.getUserById(4);
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
    }

    @Test
    public void testGetAllUser() {
        List<User> userList = userMapper.getAllUser();
        assertNotNull(userList);
        assertTrue(userList.size() > 0);
    }

    @Test
    public void testGetUserByUsername() {
        User user = userMapper.getUserByUsername("user01");
        assertNotNull(user);
        assertEquals((Integer) 5,  user.getUid());
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("Junit Test");
        user.setPassword("Junit Test");
        userMapper.insertUser(user);

        User insertedUser = userMapper.getUserByUsername(user.getUsername());
        assertNotNull(insertedUser);
        assertEquals("Junit Test", insertedUser.getUsername());
    }
}
