package com.logistics.platform.delivery_service.deliveryRoute.application.service.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryManagerResponseDto {

  private UUID deliveryManagerId;       // 배송담당자 ID
  private String deliveryManagerSlackId;   // 배송담당자 slackId
  // private String status;     // 배정 가능 여부를 위한 상태(배정가능/배송중)

}
