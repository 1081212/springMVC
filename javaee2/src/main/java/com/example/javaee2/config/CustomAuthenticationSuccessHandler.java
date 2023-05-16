//package com.example.javaee2.config;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Collection;
//
//@Component
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        System.out.println("Authorities: " + authorities);
//        for (GrantedAuthority grantedAuthority : authorities) {
//            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
//                System.out.println("admin:suc");
//                redirectStrategy.sendRedirect(request, response, "/order/getNoGo");
//                return;
//            } else if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
//                System.out.println("user:suc");
//                redirectStrategy.sendRedirect(request, response, "/ship/getAll");
//                return;
//            }else {
//                System.out.println("none:suc");
//            }
//        }
//        throw new IllegalStateException("Unknown authority");
//    }
//}
