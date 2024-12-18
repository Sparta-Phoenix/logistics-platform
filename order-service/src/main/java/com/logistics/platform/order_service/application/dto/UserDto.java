package com.logistics.platform.order_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Long id;
  private String username;
  private String number;
  private String email;
  private String slackId;
  private Role role;

}
