package org.example.authserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

public class JwtUtils {

    public static final String TOKEN_HEADER = "Authorization"; //token请求头
    public static final String TOKEN_PREFIX = "Bearer";//token前缀
    public static final long EXPIRATION = 60 * 60 * 1000; //token有效期
    public static final String SUBJECT = "piconjo"; //签名主题
    public static final String HEADER_STRING = "Passport"; //配置token在http heads中的键值
    public static final String ROLE_CLAIMS = "role"; //角色权限声明

    // 生成安全的密钥
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);


    public static String getKey(){
        byte[] keyBytes = key.getEncoded();
        String keys = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("now the key is "+keys);
        return keys;
    }


    // 生成token
    public static String createToken(String username, String role) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        String token = Jwts.builder()
                .setSubject(username)
                .setClaims(map)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
        return token;
    }

    // 校验token
    public static Claims checkJWT(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 从Token中获取username
    public static String getUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims.get("username").toString();
    }

    // 从Token中获取用户角色
    public static String getUserRole(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims.get("role").toString();
    }

    // 校验Token是否过期
    public static boolean isExpiration(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }


}
