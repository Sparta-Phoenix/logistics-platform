package com.logistics.platform.deliverymanagerservice.presentation.response;

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
public class DeliveryManagerResponseDto {

  private UUID deliveryManagerId;
  private Long userId;
  private UUID hubId;
  private UUID slackId;
  private DeliveryType deliveryType;
  private Long deliveryOrderNumber;

  @QueryProjection
  public DeliveryManagerResponseDto(DeliveryManager deliveryManager){
    this.deliveryManagerId = deliveryManager.getId();
    this.userId = deliveryManager.getUserId();
    this.hubId = deliveryManager.getHubId();
    this.slackId = deliveryManager.getSlackId();
    this.deliveryType = deliveryManager.getDeliveryType();
    this.deliveryOrderNumber = deliveryManager.getDeliveryOrderNumber();
  }


}
