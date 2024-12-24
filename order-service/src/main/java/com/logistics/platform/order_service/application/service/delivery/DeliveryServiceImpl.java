package com.logistics.platform.order_service.application.service.delivery;

import com.logistics.platform.order_service.infrastructure.client.DeliveryClient;
import com.logistics.platform.order_service.infrastructure.request.DeliveryRequestDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

  private final DeliveryClient deliveryClient;

  @Override
  @Async
  public void createDelivery(Long userId, UUID startHubId, UUID endHubId, UUID orderId,
      String recipient, String userSlackId, String address, String userName, String userRole) {
    deliveryClient.createDelivery(
        new DeliveryRequestDto(userId, startHubId, endHubId, orderId, recipient, userSlackId,
            address), userName, userRole);
  }
}
