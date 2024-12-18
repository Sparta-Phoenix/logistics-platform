package com.logistics.platform.auth_service.presentation.controller;

import com.logistics.platform.auth_service.presentation.response.SignupResDto;
import com.logistics.platform.auth_service.application.service.AuthService;
import com.logistics.platform.auth_service.presentation.global.ResponseDto;
import com.logistics.platform.auth_service.presentation.request.SignupReqDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseDto<SignupResDto> signup(@RequestBody @Valid SignupReqDto reqDto) {
        return new ResponseDto<>(ResponseDto.SUCCESS, "회원가입되었습니다.", authService.signup(reqDto));
    }

    @GetMapping("/role/validation")
    public Boolean roleCheck(@RequestHeader("X-User-Name") String userName,
        @RequestHeader("X-User-Role") String userRole) {
        return authService.validationRole(userName, userRole);
    }

  @GetMapping("/slack/slackId")
  public String getSlackIdByUsername(@RequestParam(value = "username") String username) {
    return authService.getSlackIdByUsername(username);
  }

}
