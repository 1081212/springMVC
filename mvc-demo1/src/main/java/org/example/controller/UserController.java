package org.example.controller;

import org.apache.ibatis.session.SqlSession;
import org.example.dao.UserMapper;
import org.example.pojo.User;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private SqlSessionFactoryBean sqlSessionFactory;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @RequestMapping("/user/{id}")
    public String getUser(@PathVariable Integer id, Model model) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.getObject().openSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getUserById(id);
            model.addAttribute("user", user);
            System.out.println(user.toString());
        }
        return "target";
    }

    @RequestMapping("/user")
    public String getAllUser(Model model) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.getObject().openSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.getAllUser();
            model.addAttribute("userList", userList);
            System.out.println(userList.toString());
        }
        return "target";
    }

//    @RequestMapping("/userList")
//    @ResponseBody
//    public List<User> getUserList() throws Exception {
//        try (SqlSession sqlSession = sqlSessionFactory.getObject().openSession()) {
//            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
//            List<User> userList = userMapper.getAllUser();
//            return userList;
//        }
//    }

    @PostMapping ("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        Model model, HttpServletRequest request) {

        System.out.println(username+password);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            return "index";
        } catch (Exception e) {
            model.addAttribute("loginError", "用户名或密码错误！");
            return "target";
        }
    }

    @PostMapping("/register")
    public String registerUser(User user,Model model) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.getObject().openSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User exitUser = userMapper.getUserByUsername(user.getUsername());
            if (exitUser!=null){
                model.addAttribute("error","用户已存在,换个用户名吧~");
                return "register";
            }
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userMapper.insertUser(user);
            System.out.println(user.toString());
            return "login";
        }
    }


}
