package com.logistics.platform.delivery_service.delivery.presentation.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.logistics.platform.delivery_service.delivery.domain.model.Delivery;
import com.logistics.platform.delivery_service.delivery.domain.model.DeliveryStatus;
import com.logistics.platform.delivery_service.deliveryRoute.presentation.response.DeliveryRouteResponseDto;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
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
public class DeliveryResponseDto {

  private UUID deliveryId;
  private Long userId;
  private UUID deliveryRouteId;
  private UUID startHubId;
  private UUID endHubId;
  private UUID orderId;
  private DeliveryStatus deliveryStatus;
  private String recipient;
  private String recipientSlackId;
  private String address;
  private String message;

  // 배송 경로 리스트 추가
  private List<DeliveryRouteResponseDto> deliveryRoutes;

  @QueryProjection
  public DeliveryResponseDto(Delivery delivery, List<DeliveryRouteResponseDto> deliveryRoutes) {
    this.deliveryId = delivery.getId();
    this.userId = delivery.getUserId();
    this.startHubId = delivery.getStartHubId();
    this.endHubId = delivery.getEndHubId();
    this.orderId = delivery.getOrderId();
    this.deliveryStatus = delivery.getDeliveryStatus();
    this.recipient = delivery.getRecipient();
    this.recipientSlackId = delivery.getRecipientSlackId();
    this.address = delivery.getAddress();
    this.deliveryRoutes = deliveryRoutes;
  }


  public DeliveryResponseDto(String message) {
    this.message = message;
  }
}
