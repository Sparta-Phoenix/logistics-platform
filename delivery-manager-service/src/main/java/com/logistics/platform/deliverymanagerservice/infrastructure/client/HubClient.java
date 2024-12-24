package com.logistics.platform.deliverymanagerservice.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubClient {

  @GetMapping("/api/hubs/{hubId}/exists")
  boolean checkIfHubExists(@PathVariable("hubId") UUID hubId);
  // 허브서비스 필요

}
