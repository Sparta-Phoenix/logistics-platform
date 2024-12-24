package com.logistics.platform.order_service.infrastructure.client;

import com.logistics.platform.order_service.application.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", path = "/api/users")
public interface UserClient {

  @GetMapping("/info")
  UserDto getUserInfo(
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole);
}
