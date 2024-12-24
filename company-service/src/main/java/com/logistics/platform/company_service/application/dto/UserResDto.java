package com.logistics.platform.company_service.application.dto;

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
