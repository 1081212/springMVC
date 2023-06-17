package org.example.gateway.service;

import org.example.gateway.entity.MyUserDetails;
import org.example.gateway.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService usersService;


    /**
     * 用户登录
     * @param s 获取到的UserName,
     * @return user 返回的是org.springframework.security.core.userdetails.User;
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (s==null || "".equals("s")){
            throw new RuntimeException("用户不能为空");
        }
        //根据用户名查询对象
        User user = usersService.getUserByUsername(s);
//        if (user==null){
//            throw new RuntimeException("用户不存在");
//        }
//        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
//        //根据userId获取Role对象
//        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
//
//        //这里没有对用户密码进行判断，将数据库中查到的密码封装到了对象中，在WebSecurityConfig中configure()方法中，User进行了验证，
//        //前端传递的密码是明文，数据中存的是暗文,需要对前端传递的密码加密，进行验证,加密方法是new BCryptPasswordEncoder().encode("pwd")
//        return new MyUserDetails(user.getUsername(),user.getPassword(),authorities);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + user.getUsername());
        }

        return new MyUserDetails(user);
    }

}
