package org.example.authserver.Filter;

import com.alibaba.fastjson.JSON;
import org.example.authserver.mapper.UserMapper;
import org.example.authserver.entity.MyUserDetails;
import org.example.authserver.entity.Result;
import org.example.authserver.service.UserService;
import org.example.authserver.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.authserver.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private  RedisUtils redisUtils;
    private AuthenticationManager authenticationManager;


    public JwtAuthenticationFilter (AuthenticationManager authenticationManager,RedisUtils redisUtils) {
        this.authenticationManager = authenticationManager;
        this.redisUtils = redisUtils;
        //默认的登录路径是/login,post请求
        super.setFilterProcessesUrl("/api/login");
    }

    //验证操作， 接受并解析用户凭证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //从输入流中获取到登录的信息
        //创建一个token并条用authenticationManager.authenticate 让Spring Security进行验证
        try {
            BufferedReader reader = request.getReader();
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            System.out.println("line:"+line);


                // 解析JSON数据
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> jsonData = mapper.readValue(requestBody.toString(), Map.class);
                System.out.println("jsonData:"+jsonData);
                return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        jsonData.get("username"),jsonData.get("password")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 验证【成功】后调用的方法
     * 若验证成功 生成token并返回
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        MyUserDetails user= (MyUserDetails) authResult.getPrincipal();
        System.out.println(user.toString());
        // 从User中获取权限信息
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        // 创建Token
        String token = JwtUtils.createToken(user.getUsername(), authorities.toString());
        String key = JwtUtils.getKey();
        redisUtils.set("JWTKey",key);
        UserMapper userMapper = UserService.getUserMapper();
        Integer uid = userMapper.getUserByUsername(user.getUsername()).getUid();
        String role = JwtUtils.getUserRole(token);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("uid",uid);
        map.put("role",role);

        // 设置编码 防止乱码问题
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // 在请求头里返回创建成功的token
        // 设置请求头为带有"Bearer "前缀的token字符串
        response.setHeader("token", JwtUtils.TOKEN_PREFIX + token);
        // 处理编码方式 防止中文乱码
        response.setContentType("text/json;charset=utf-8");
        String result = JSON.toJSONString(new Result(true,"suc",200,map));
        // 将反馈塞到HttpServletResponse中返回给前台
        response.getWriter().write(result);
    }
    /**
     * 验证【失败】调用的方法
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String returnData="";
        // 账号过期
        if (failed instanceof AccountExpiredException) {
            returnData="账号过期";
        }
        // 密码错误
        else if (failed instanceof BadCredentialsException) {
            returnData="密码错误";
        }
        // 密码过期
        else if (failed instanceof CredentialsExpiredException) {
            returnData="密码过期";
        }
        // 账号不可用
        else if (failed instanceof DisabledException) {
            returnData="账号不可用";
        }
        //账号锁定
        else if (failed instanceof LockedException) {
            returnData="账号锁定";
        }
        // 用户不存在
        else if (failed instanceof InternalAuthenticationServiceException) {
            returnData="用户不存在";
        }
        // 其他错误
        else{
            returnData="未知异常";
        }

        // 处理编码方式 防止中文乱码
        response.setContentType("text/json;charset=utf-8");
        // 将反馈塞到HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(returnData));
    }
}
