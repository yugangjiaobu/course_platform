package com.example.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Date;

public class JWTUtil {
    private static final String SECRET_KEY = "yugangjiaobu"; // 密钥，自行设置
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 过期时间，单位毫秒

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            // 签名异常
        } catch (MalformedJwtException e) {
            // JWT格式错误
        } catch (ExpiredJwtException e) {
            // JWT过期
        } catch (UnsupportedJwtException e) {
            // 不支持的JWT
        } catch (IllegalArgumentException e) {
            // 令牌为空
        }
        return false;
    }
}
