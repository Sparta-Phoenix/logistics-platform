package com.logistics.platform.auth_service.presentation.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SigninReqDto {
    @Size(min = 4, max = 10, message = "아이디는 최소 4자 이상, 최대 10자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 소문자 알파벳과 숫자로만 구성되어야 합니다.")
    private String username;

    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 최대 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=|<>?{}\\[\\]~-]).+$",
        message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

}
