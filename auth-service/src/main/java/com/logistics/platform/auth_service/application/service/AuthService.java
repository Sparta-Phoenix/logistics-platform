package com.logistics.platform.auth_service.application.service;

import com.logistics.platform.auth_service.application.dto.SignupResDto;
import com.logistics.platform.auth_service.common.exception.CustomApiException;
import com.logistics.platform.auth_service.domain.model.Role;
import com.logistics.platform.auth_service.domain.model.User;
import com.logistics.platform.auth_service.domain.repository.UserRepository;
import com.logistics.platform.auth_service.presentation.request.SignupReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResDto signup(SignupReqDto reqDto) {
        userRepository.findByUsername(reqDto.getUsername())
            .ifPresent(user -> {
                throw new CustomApiException("이미 사용 중인 사용자 이름입니다");
            });

        Role role = validateRole(reqDto.getRole());

        User user = User.builder()
            .username(reqDto.getUsername())
            .password(passwordEncoder.encode(reqDto.getPassword()))
            .email(reqDto.getEmail())
            .number(reqDto.getNumber())
            .slackId(reqDto.getSlackId())
            .role(role)
            .isDeleted(false)
            .build();

        return new SignupResDto(userRepository.save(user).getId());
    }

    private Role validateRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomApiException("유효하지 않은 role 입니다");
        }
    }

}
