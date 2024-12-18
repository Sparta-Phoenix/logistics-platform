package com.logistics.platform.auth_service.infrastructure.config.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.platform.auth_service.application.dto.CustomUserDetails;
import com.logistics.platform.auth_service.infrastructure.config.jwt.JwtUtil;
import com.logistics.platform.auth_service.presentation.request.SigninReqDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public static final String AUTHORIZATION = "Authorization";

    public static final String BEARER = "Bearer ";

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/auth/signin");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SigninReqDto reqDto = objectMapper.readValue(request.getInputStream(),
                SigninReqDto.class);
            log.info("username {}", reqDto.getUsername());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                reqDto.getUsername(), reqDto.getPassword(), null);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        log.info("user role {} ", role);
        log.info("user role {} ", username);

        String token = jwtUtil.createJwt(username, role);

        response.addHeader(AUTHORIZATION, BEARER + token);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) throws IOException {

        log.error("로그인 실패 : {}", failed.getMessage());

        response.setContentType("application/json; charset=UTF-8"); // UTF-8 설정
        response.setCharacterEncoding("UTF-8"); // 추가 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(
            "{\"code\": -1, \"message\": \"로그인 실패: " + failed.getMessage() + "\", \"data\": null}");
    }

}
