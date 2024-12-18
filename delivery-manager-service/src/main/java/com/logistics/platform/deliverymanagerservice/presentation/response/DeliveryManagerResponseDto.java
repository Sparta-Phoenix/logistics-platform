package com.logistics.platform.deliverymanagerservice.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryManager;
import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryType;
import com.querydsl.core.annotations.QueryProjection;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryManagerResponseDto {

  private UUID deliveryManagerId;
  private Long userId;
  private UUID hubId;
  private String deliveryManagerSlackId;
  private DeliveryType deliveryType;
  private Long deliveryOrderNumber;
  private String message;

  @QueryProjection
  public DeliveryManagerResponseDto(DeliveryManager deliveryManager){
    this.deliveryManagerId = deliveryManager.getId();
    this.userId = deliveryManager.getUserId();
    this.hubId = deliveryManager.getHubId();
    this.deliveryManagerSlackId = deliveryManager.getSlackId();
    this.deliveryType = deliveryManager.getDeliveryType();
    this.deliveryOrderNumber = deliveryManager.getDeliveryOrderNumber();
  }

  // UUID 기반 생성자 추가
  public DeliveryManagerResponseDto(UUID deliveryManagerId, String deliveryManagerSlackId) {
    this.deliveryManagerId = deliveryManagerId;
    this.deliveryManagerSlackId = deliveryManagerSlackId;
  }


  public DeliveryManagerResponseDto(String message) {
    this.message = message;
  }
}
