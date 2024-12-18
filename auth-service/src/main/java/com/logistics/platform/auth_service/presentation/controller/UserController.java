package com.logistics.platform.auth_service.presentation.controller;

import com.logistics.platform.auth_service.application.dto.CustomUserDetails;
import com.logistics.platform.auth_service.presentation.response.UserResDto;
import com.logistics.platform.auth_service.application.service.UserService;
import com.logistics.platform.auth_service.presentation.global.ResponseDto;
import com.logistics.platform.auth_service.presentation.request.DeleteReqDto;
import com.logistics.platform.auth_service.presentation.request.PasswordUpdateReqDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseDto<Page<UserResDto>> getUsers(
        @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
        Pageable pageable) {
        return new ResponseDto<>(ResponseDto.SUCCESS, "사용자 목록이 조회되었습니다.",
            userService.getUsers(keyword, pageable));
    }

    @GetMapping("/{userId}")
    public ResponseDto<UserResDto> getUser(@PathVariable Long userId) {
        return new ResponseDto<>(ResponseDto.SUCCESS, "사용자가 조회되었습니다.", userService.getUser(userId));
    }

    @PutMapping("/password")
    public ResponseDto<Void> updatePassword(@RequestBody @Valid PasswordUpdateReqDto reqDto,
        @AuthenticationPrincipal
        CustomUserDetails userDetails) {
        userService.updatePassword(reqDto, userDetails.getUserId());

        return new ResponseDto<>(ResponseDto.SUCCESS, "비밀번호가 수정되었습니다.", null);
    }

    @DeleteMapping("")
    public ResponseDto<?> deleteUser(@RequestBody @Valid DeleteReqDto reqDto,
        @AuthenticationPrincipal
        CustomUserDetails userDetails) {
        userService.deleteUser(reqDto, userDetails.getUserId());
        return new ResponseDto<>(ResponseDto.SUCCESS, "탈퇴되었습니다.", null);
    }

    @GetMapping("/info")
    public UserResDto getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getUser(customUserDetails.getUserId());
    }

    @GetMapping("/{userId}/info")
    public UserResDto getUserInfo(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/infoUserId")
    public UserResDto getUserId(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getUserId(customUserDetails.getUsername());

    }
}
