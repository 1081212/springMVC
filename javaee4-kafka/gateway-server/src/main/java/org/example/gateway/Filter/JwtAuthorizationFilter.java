package org.example.gateway.Filter;

import org.checkerframework.checker.units.qual.A;
import org.example.gateway.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.gateway.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtAuthorizationFilter implements WebFilter {

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("do filter");

        ServerHttpRequest request = exchange.getRequest();
        String tokenHeader = request.getHeaders().getFirst(JwtUtils.TOKEN_HEADER);
        String path = request.getPath().value();
        System.out.println(path);

        if (tokenHeader == null || !tokenHeader.startsWith(JwtUtils.TOKEN_PREFIX)) {
            System.out.println("none token header");
            if (path.equals("/api/login")) return chain.filter(exchange);
            return exchange.getResponse().setComplete();
        }

        try {
            JwtUtils.initializeKey(redisUtils);
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            String value = "Token Error";
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(value.getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        return  chain.filter(exchange);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(JwtUtils.TOKEN_PREFIX, "");
        String username = JwtUtils.getUsername(token);
        String role = JwtUtils.getUserRole(token);
        List<SimpleGrantedAuthority> authorities = Stream.of(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        if (username != null) {
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }
}
