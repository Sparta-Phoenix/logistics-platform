package com.logistics.platform.slack_service.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AuthClient {

  @GetMapping("/api/auth/slack/slackId")
  String getSlackIdByUsername(
      @RequestParam(value = "username") String username);

}
