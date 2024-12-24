package com.logistics.platform.delivery_service.delivery.infrastructure.client;


import com.logistics.platform.delivery_service.delivery.application.dto.HubRouteCreateRequest;
import com.logistics.platform.delivery_service.delivery.application.dto.HubRouteResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hub-service")
public interface HubClient {

  @GetMapping("/api/hubRoutes")
  List<HubRouteResponseDto> getHubRoutes(
      @RequestBody HubRouteCreateRequest hubRouteCreateRequest,
      @RequestHeader("X-User-Name") String userName,
      @RequestHeader("X-User-Role") String userRole
  );
}



