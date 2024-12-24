package com.logistics.platform.order_service.infrastructure.request;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryRequestDto {

  private Long userId;
  private UUID startHubId;
  private UUID endHubId;
  private UUID orderId;
  private String recipient;
  private String recipientSlackId;
  private String address;

  public DeliveryRequestDto(Long userId, UUID startHubId, UUID endHubId, UUID orderId,
      String recipient, String userSlackId, String address) {
    this.userId = userId;
    this.startHubId = startHubId;
    this.endHubId = endHubId;
    this.orderId = orderId;
    this.recipient = recipient;
    this.recipientSlackId = userSlackId;
    this.address = address;
  }
}
