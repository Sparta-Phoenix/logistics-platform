package com.logistics.platform.deliverymanagerservice.infrastructure.client;


import com.logistics.platform.deliverymanagerservice.application.dto.UserResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", path = "/api/users")
public interface UserClient {

//  @GetMapping("/users/{userId}/exists")
//  boolean checkIfUserExists(@PathVariable("userId") Long userId);

  @GetMapping("/info")
  UserResDto getUserInfo(
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole);

}
