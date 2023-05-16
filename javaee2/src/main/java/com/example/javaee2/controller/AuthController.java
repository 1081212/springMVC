package com.example.javaee2.controller;

import com.example.javaee2.pojo.JwtResponse;
import com.example.javaee2.pojo.Result;
import com.example.javaee2.pojo.User;
import com.example.javaee2.service.UserService;
import com.example.javaee2.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

//    @PostMapping("/logine")
//    public ResponseEntity<?> logine(@RequestBody User loginUser) {
//        try {
//            // 验证用户账号密码
//            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());
//            Authentication result = authenticationManager.authenticate(authentication);
//
//            // 生成token
//            String token = JwtUtils.createToken(loginUser.getUsername(), userService.getUserByUsername(loginUser.getUsername()).getRole());
//
//            // 返回响应
//            return ResponseEntity.ok(new JwtResponse(token));
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        try {
            // 验证用户账号密码
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());
            Authentication result = authenticationManager.authenticate(authentication);

            // 生成token
            String token = JwtUtils.createToken(loginUser.getUsername(), userService.getUserByUsername(loginUser.getUsername()).getRole());

            System.out.println("login suc:"+token);
            return new Result(true,"login successful!",200,token);
        } catch (AuthenticationException e) {
            return new Result(false,"login false!",403);
        }
    }
}
