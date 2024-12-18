package com.logistics.platform.delivery_service.deliveryRoute.infrastructure.client;

import com.logistics.platform.delivery_service.deliveryRoute.application.service.dto.DeliveryManagerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "delivery-manager-service")
public interface DeliveryManagerClient {

  @GetMapping("/api/delivery-managers/next")
  DeliveryManagerResponseDto getNextAvailableDeliveryManager();

}