package com.logistics.platform.company_service.infrastructure.client;

import com.logistics.platform.company_service.application.dto.UserResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", path = "/api/users")
public interface UserClient {

  @GetMapping("/infoUserId")
  UserResDto getUserId(
      @RequestHeader(value = "X-User-Role") String role,
      @RequestHeader(value = "X-User-Name") String userName);
}
