package com.logistics.platform.gateway_service.filter;

import com.logistics.platform.gateway_service.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthPreFilter implements GlobalFilter {

    private static final String AUTH_SIGNUP = "/api/auth/signup";
    private static final String AUTH_SIGNIN = "/api/auth/signin";
    private static final String AUTH_SWAGGER = "/swagger/";

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("uri {}", path);

        if (path.startsWith(AUTH_SIGNUP) || path.startsWith(AUTH_SIGNIN) || path.startsWith(AUTH_SWAGGER)) {
            log.info("auth 인증 제외");
            return chain.filter(exchange);
        }

        String token = jwtUtil.extractToken(exchange.getRequest());
        log.info("token {}", token);

        if (token == null) {
            log.info("로그인 X");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String userName = "";
        String userRole = "";
        try {
            userName = jwtUtil.extractUsername(token);
            userRole = jwtUtil.extractRole(token);
            log.info("사용자 이름: {}, 역할: {}", userName, userRole);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(addHeaders(exchange, userName, userRole));
    }

    private ServerWebExchange addHeaders(ServerWebExchange exchange, String userName, String userRole) {

        return exchange.mutate()
            .request(exchange.getRequest().mutate()
                .header("X-User-Name", userName)
                .header("X-User-Role", userRole)
                .build())
            .build();
    }



}
