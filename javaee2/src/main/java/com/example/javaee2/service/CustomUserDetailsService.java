//package com.example.javaee2.service;
//
//import org.example.dao.UserMapper;
//import org.example.pojo.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userMapper.getUserByUsername(username);
//        System.out.println("ld:"+user);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
//
//        return new org.springframework.security.core.userdetails.User(user.getUsername(),
//                user.getPassword(), user.isEnabled(), true, true, true, authorities);
//    }
//
//}
