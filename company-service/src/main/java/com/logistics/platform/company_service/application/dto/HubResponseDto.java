package com.logistics.platform.company_service.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubResponseDto {
  private UUID hubId;

  private Long hubManagerId;

  private String hubName;

  private HubType hubType;

  private String centerPostalCode;

  private String roadAddress;

  private String postalCode;

  private Double latitude;

  private Double longitude;

}
