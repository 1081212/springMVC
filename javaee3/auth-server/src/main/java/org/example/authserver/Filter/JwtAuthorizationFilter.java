package org.example.authserver.Filter;

import org.example.authserver.util.JwtUtils;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

        System.out.println("dofilter");
        String tokenHeader=request.getHeader(JwtUtils.TOKEN_HEADER);
        //因为设置拦截全部请求，所有这里没有token放行之后是会返回没有登录
        if (tokenHeader==null || !tokenHeader.startsWith(JwtUtils.TOKEN_PREFIX)){
            System.out.println("none token");
            chain.doFilter(request,response);
            return;
        }
        try {
            // 解析并设置认证信息
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        } catch (SignatureException e) {
            // 发生 SignatureException 异常，返回 token 错误
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token Error");
            return;
        }

        // 继续处理请求
        chain.doFilter(request, response);
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
