package com.logistics.platform.auth_service.infrastructure.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    public static final String AUTHORIZATION_ROLE = "role";

    private final SecretKey secretKey;

    public JwtUtil(@Value("${service.jwt.secret-key}") String secretKey) {
        this.secretKey = new SecretKeySpec(Decoders.BASE64.decode(secretKey), SIG.HS256.key().build().getAlgorithm());
    }

    public String createJwt(String username, String role) {

        return Jwts.builder()
            .subject(username)
            .claim(AUTHORIZATION_ROLE, role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + accessExpiration))
            .signWith(secretKey)
            .compact();
    }
}
