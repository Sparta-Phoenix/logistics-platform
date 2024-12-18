package com.logistics.platform.auth_service.application.service;

import com.logistics.platform.auth_service.presentation.response.UserResDto;
import com.logistics.platform.auth_service.presentation.global.exception.CustomApiException;
import com.logistics.platform.auth_service.domain.model.User;
import com.logistics.platform.auth_service.domain.repository.UserRepository;
import com.logistics.platform.auth_service.presentation.request.DeleteReqDto;
import com.logistics.platform.auth_service.presentation.request.PasswordUpdateReqDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResDto> getUsers(String keyword, Pageable pageable) {

        int size = pageable.getPageSize();

        size = (size == 30 || size == 50) ? size : 10;

        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by(
            Sort.Order.desc("createdAt"),
            Sort.Order.desc("updatedAt")
        );

        pageable = PageRequest.of(pageable.getPageNumber(), size, sort);

        Page<User> users = userRepository.findByIsDeletedFalse(pageable);
        return users.map(user -> new UserResDto(user.getId(), user.getUsername(), user.getNumber(),
            user.getEmail(), user.getSlackId(), user.getRole()));
    }

    public UserResDto getUser(Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
            .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다"));

        return new UserResDto(user.getId(), user.getUsername(), user.getNumber(), user.getEmail(),
            user.getSlackId(), user.getRole());
    }

    @Transactional
    public void updatePassword(@Valid PasswordUpdateReqDto reqDto, Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
            .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다"));

        if (!passwordEncoder.matches(reqDto.getOldPassword(), user.getPassword())) {
            throw new CustomApiException("비밀번호가 일치하지 않습니다");
        }

        user.updatePassword(passwordEncoder.encode(reqDto.getNewPassword()));
    }


    @Transactional
    public void deleteUser(@Valid DeleteReqDto reqDto, Long userId) {

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
            .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다"));

        if (!passwordEncoder.matches(reqDto.getPassword(), user.getPassword())) {
            throw new CustomApiException("비밀번호가 일치하지 않습니다");
        }

        user.deleteUser();
    }

    public UserResDto getUserId(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(
            () -> new CustomApiException("존재하지 않는 사용자입니다"));
        return new UserResDto(user.getId(), user.getUsername(), user.getNumber(), user.getEmail(),
            user.getSlackId(), user.getRole());
    }
}
