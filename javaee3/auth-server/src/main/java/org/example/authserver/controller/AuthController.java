package org.example.authserver.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.example.authserver.entity.Result;
import org.example.authserver.entity.User;
import org.example.authserver.service.UserService;
import org.example.authserver.util.JwtUtils;
import org.example.authserver.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

    @Autowired
    private RedisUtils redisUtils;

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

    @PostMapping("/register")
    public Result register(@RequestBody User loginUser) {
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
