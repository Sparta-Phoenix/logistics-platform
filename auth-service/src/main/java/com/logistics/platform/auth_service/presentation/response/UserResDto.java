package com.logistics.platform.auth_service.presentation.response;

import com.logistics.platform.auth_service.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResDto {

    private Long id;
    private String username;
    private String number;
    private String email;
    private String slackId;
    private Role role;

}
