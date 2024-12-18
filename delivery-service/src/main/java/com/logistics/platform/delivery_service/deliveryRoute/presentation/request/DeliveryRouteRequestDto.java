package com.logistics.platform.delivery_service.deliveryRoute.presentation.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.logistics.platform.delivery_service.deliveryRoute.domain.model.DeliveryRouteStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryRouteRequestDto {

  private UUID deliveryId;
  private UUID startHubId;
  private UUID endHubId;
  private UUID deliveryManagerId;
  private Double estimatedDuration;
  private Double estimatedDistance;
  private DeliveryRouteStatus status; // 여기서 이거 삭제하고 기본값 허브대기중 이런걸로 해두고, updateRequestDto 따로 만들지...
  private Long sequence;

}
