package com.logistics.platform.product_service.application.dto;

import com.logistics.platform.product_service.domain.model.Role;
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
