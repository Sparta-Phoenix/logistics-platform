package com.logistics.platform.auth_service.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordUpdateReqDto {

    @NotBlank
    private String oldPassword;


    @NotBlank
    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 최대 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=|<>?{}\\[\\]~-]).+$",
        message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String newPassword;

}
