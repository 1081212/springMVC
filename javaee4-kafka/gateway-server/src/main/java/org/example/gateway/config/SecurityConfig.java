package org.example.gateway.config;

import org.example.gateway.Filter.JwtAuthorizationFilter;
import org.example.gateway.entity.MyUserDetails;
import org.example.gateway.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableWebFluxSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .addFilterAt(jwtAuthorizationFilter, SecurityWebFiltersOrder.SECURITY_CONTEXT_SERVER_WEB_EXCHANGE)
                .authorizeExchange()
                .pathMatchers("/api/login").permitAll()
                .pathMatchers("/user-server/getUser").permitAll()
                .pathMatchers("/favicon.ico").permitAll()
                .pathMatchers("/images/**").permitAll()
                .pathMatchers("/").permitAll()
                .pathMatchers("/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((exchange, e) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                })
                .and()
                .securityContextRepository(securityContextRepository());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public ServerSecurityContextRepository securityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }


    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }
}
