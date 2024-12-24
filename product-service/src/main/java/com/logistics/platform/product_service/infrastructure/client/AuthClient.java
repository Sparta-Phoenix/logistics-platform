package com.logistics.platform.product_service.infrastructure.client;

import com.logistics.platform.product_service.application.dto.UserResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", path = "/api/users")
public interface AuthClient {

  @GetMapping("/info")
  UserResDto getUserInfo(
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole);

  @GetMapping("/{userId}/info")
  UserResDto getUSerInfoById(
      @PathVariable Long userId,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole);


}
