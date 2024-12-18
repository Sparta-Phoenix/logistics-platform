package com.logistics.platform.delivery_service.delivery.infrastructure.client;

import com.logistics.platform.delivery_service.delivery.application.dto.SlackRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "slack-service")
public interface SlackClient {

  @PostMapping("/api/slacks/deliveryManager")
  void sendSlackMessage(
      @RequestBody SlackRequestDto slackRequestDto,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole);

}
