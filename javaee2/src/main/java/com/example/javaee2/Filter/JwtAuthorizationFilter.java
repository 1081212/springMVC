package com.example.javaee2.Filter;

import com.example.javaee2.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String tokenHeader=request.getHeader(JwtUtils.TOKEN_HEADER);
        //因为设置拦截全部请求，所有这里没有token放行之后是会返回没有登录
        if (tokenHeader==null || !tokenHeader.startsWith(JwtUtils.TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }
        //若请求头中由token，则调用下面的方法进行解析，并设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.clearContext();
        } else {
            super.doFilterInternal(request,response,chain);
        }
    }


    public UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader){
        //去掉前缀，获取token字符串
        String token = tokenHeader.replace(JwtUtils.TOKEN_PREFIX, "");
        //从token中解密获取用户用户名
        String username=JwtUtils.getUsername(token);
        //从token中解密获取用户角色
        String role=JwtUtils.getUserRole(token);
        String[] roles = StringUtils.strip(role, "[]").split(",");
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
        for (String s : roles) {
            authorities.add(new SimpleGrantedAuthority(s));
        }
        if (username!=null){
            //这里像SpringSecurity声明用户角色，做相关操作放行
            return new UsernamePasswordAuthenticationToken(username,null,authorities);
        }
        return null;
    }

}
