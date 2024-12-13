package com.logistics.platform.company_service.infrastructure.client;

import com.logistics.platform.company_service.application.dto.HubResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubClient {

  @GetMapping("/api/hubs/{hubId}/info")
  HubResponseDto getHubResponseDto(@PathVariable UUID hubId);

}
