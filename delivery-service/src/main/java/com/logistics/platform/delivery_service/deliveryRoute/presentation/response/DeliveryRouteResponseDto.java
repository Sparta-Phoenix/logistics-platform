package com.logistics.platform.delivery_service.deliveryRoute.presentation.response;

import com.logistics.platform.delivery_service.deliveryRoute.domain.model.DeliveryRoute;
import com.logistics.platform.delivery_service.deliveryRoute.domain.model.DeliveryRouteStatus;
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
public class DeliveryRouteResponseDto {

  private UUID id;
  private UUID deliveryId;
  private UUID startHubId;
  private UUID endHubId;
  private UUID deliveryManagerId;
  private String deliveryManagerSlackId;
  private Double estimatedDuration;
  private Double estimatedDistance;
  private Double actualDuration;
  private Double actualDistance;
  private DeliveryRouteStatus status;
  private Long sequence;

  @QueryProjection
  public DeliveryRouteResponseDto(DeliveryRoute deliveryRoute) {
    this.id = deliveryRoute.getId();
    this.deliveryId = deliveryRoute.getDelivery().getId();
    this.startHubId = deliveryRoute.getStartHubId();
    this.endHubId = deliveryRoute.getEndHubId();
    this.deliveryManagerId = deliveryRoute.getDeliveryManagerId();
    this.estimatedDuration = deliveryRoute.getEstimatedDuration();
    this.estimatedDistance = deliveryRoute.getEstimatedDistance();
    this.actualDuration = deliveryRoute.getActualDuration();
    this.actualDistance = deliveryRoute.getActualDistance();
    this.status = deliveryRoute.getStatus();
    this.sequence = deliveryRoute.getSequence();
    this.deliveryManagerSlackId = null;
  }

  public DeliveryRouteResponseDto(DeliveryRoute deliveryRoute, String deliveryManagerSlackId) {
    this.id = deliveryRoute.getId();
    this.deliveryId = deliveryRoute.getDelivery().getId();
    this.startHubId = deliveryRoute.getStartHubId();
    this.endHubId = deliveryRoute.getEndHubId();
    this.deliveryManagerId = deliveryRoute.getDeliveryManagerId();
    this.estimatedDuration = deliveryRoute.getEstimatedDuration();
    this.estimatedDistance = deliveryRoute.getEstimatedDistance();
    this.actualDuration = deliveryRoute.getActualDuration();
    this.actualDistance = deliveryRoute.getActualDistance();
    this.status = deliveryRoute.getStatus();
    this.sequence = deliveryRoute.getSequence();
    this.deliveryManagerSlackId = deliveryManagerSlackId;
  }
}
