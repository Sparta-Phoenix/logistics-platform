package com.logistics.platform.hub_service.presentation.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class HubCreateRequest {

  @NotNull
  private Long hubManagerId;

  @NotEmpty
  private String hubName;

  @NotEmpty
  private String centerPostalCode;

  @NotEmpty
  private String roadAddress;

  @NotEmpty
  @Pattern(regexp = "\\d{5}", message = "주소는 5자리 숫자 우편번호 형식이어야 합니다.")
  private String postalCode;

  private Boolean isHubTypeReceiver; // false = 일반 허브, true = 중앙 허브

}
