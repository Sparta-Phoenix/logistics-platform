package com.logistics.platform.product_service.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hub-service", path = "/api/hubs")
public interface HubClient {

  @GetMapping("/{hubManagerId}/hubIds")
  UUID getHubIdByManagerId(
      @PathVariable Long hubManagerId,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String role
  );

}
