package com.example.javaee2.controller;

import com.example.javaee2.pojo.Result;
import com.example.javaee2.pojo.User;
import com.example.javaee2.service.UserService;
import org.apache.ibatis.session.SqlSession;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(ShipController.class);


    @RequestMapping("/user/{id}")
    public Result getUser(@PathVariable Integer id) throws Exception {
        try{
            User user = userService.getUserById(id);
            System.out.println(user.toString());
            if (user!=null){
                return new Result(true,"Search successful!",200,user);
            }else {
                return new Result(true,"Don't have any user!",404);
            }
        }catch (Exception e){
            logger.error("Error while executing getUser()",e);
            return new Result(false,"Search error:"+e.getMessage(),422);
        }
    }

    @RequestMapping("/user")
    public Result getAllUser() throws Exception {
        try {
            List<User> userList = userService.getAllUser();
//            model.addAttribute("userList", userList);
            System.out.println(userList.toString());
            return new Result(true,"Search successful!",200,userList);
        }catch (Exception e){
            logger.error("Error while executing getUser()",e);
            return new Result(false,"Search error:"+e.getMessage(),422);
        }
    }


    @PostMapping("/register")
    public Result registerUser(@RequestBody User user) {
        try {
            User exitUser = userService.getUserByUsername(user.getUsername());
            if (exitUser!=null){
                return new Result(false,"user already exit!",409);
            }
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userService.insertUser(user);
            System.out.println(user.toString());
            return new Result(true,"register successful!",200);
        }catch (Exception e){
            logger.error("Error while executing registerUser()",e);
            return new Result(false,"Search error:"+e.getMessage(),422);
        }
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

//    @PostMapping ("/login")
//    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
//                        Model model, HttpServletRequest request) {
//
//        System.out.println(username+password);
//
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
//        try {
//            Authentication authentication = authenticationManager.authenticate(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            HttpSession session = request.getSession(true);
//            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
//                    SecurityContextHolder.getContext());
//
//            return "redirect:/ship/getAll";
//        } catch (Exception e) {
//            model.addAttribute("loginError", "用户名或密码错误！");
//            return "forward:/login";
//        }
//    }



}
