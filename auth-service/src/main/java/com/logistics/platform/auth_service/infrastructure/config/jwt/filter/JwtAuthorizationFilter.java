package com.logistics.platform.auth_service.infrastructure.config.jwt.filter;

import com.logistics.platform.auth_service.application.dto.CustomUserDetails;
import com.logistics.platform.auth_service.application.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String username = request.getHeader("X-User-Name");
        String role = request.getHeader("X-User-Role");
        log.info("username {}, role {}", username, role);


        if (username != null && role != null) {
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

            if(!role.equals(userDetails.getRole())) {
                log.info("userDetails role {}", userDetails.getRole());
                response.setContentType("application/json; charset=UTF-8"); // UTF-8 설정
                response.setCharacterEncoding("UTF-8"); // 추가 설정
                response.setStatus(HttpStatus.FORBIDDEN.value()); // 403 상태 코드
                response.getWriter().write("{\"code\": -1, \"message\": \"Role 정보가 일치하지 않습니다\", \"data\": null}");
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
