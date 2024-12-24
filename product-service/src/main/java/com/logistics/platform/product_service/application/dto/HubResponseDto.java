package com.logistics.platform.product_service.application.dto;

import com.logistics.platform.product_service.domain.model.HubType;
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

  private String roadAddress;

  private String postalCode;

  private double latitude;

  private double longitude;

}
