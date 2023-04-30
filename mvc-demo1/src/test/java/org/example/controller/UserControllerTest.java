package org.example.controller;

import cn.hutool.json.JSONUtil;
import org.example.pojo.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/springMVC.xml")
public class UserControllerTest {
    @Autowired
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void getUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/4")).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void getAllUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user")).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void login() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "88888888");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login?username=admin&password=88888888")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .requestAttr("authentication", authentication)).andReturn();
        assertEquals(302,mvcResult.getResponse().getStatus());
    }

    @Test
    public void registerUser() throws Exception {
        User user = new User();
        user.setUsername("JUnit Test Controller");
        user.setPassword("JUnit Test Controller");
        String a = JSONUtil.toJsonStr(user);;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(a)).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }
}