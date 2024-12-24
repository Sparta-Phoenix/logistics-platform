package com.logistics.platform.order_service.infrastructure.client;

import com.logistics.platform.order_service.infrastructure.request.DeliveryRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "delivery-service", path = "/api/deliveries")
public interface DeliveryClient {

  @PostMapping
  void createDelivery(
      @RequestBody DeliveryRequestDto deliveryRequestDto,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
      );

}
