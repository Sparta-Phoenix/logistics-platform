package com.logistics.platform.gateway_service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_ROLE = "role";

    private final SecretKey secretKey;

    public JwtUtil(@Value("${service.jwt.secret-key}") String secretKey) {
        this.secretKey = new SecretKeySpec(
            Decoders.BASE64.decode(secretKey), SIG.HS256.key().build().getAlgorithm());
    }


    public String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(7);
        } else {
            return null;
        }
    }

    public Claims validateToken(String token) {

        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }

    // 토큰에서 역할(role) 추출
    public String extractRole(String token) {
        return (String) validateToken(token).get(AUTHORIZATION_ROLE);
    }

}
