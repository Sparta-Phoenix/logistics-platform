package com.logistics.platform.order_service.application.service.user;

import com.logistics.platform.order_service.application.dto.UserDto;
import com.logistics.platform.order_service.infrastructure.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserSerivce {

  private final UserClient userClient;

  @Override
  public UserDto getUserInfo(String userName, String userRole) {
    return userClient.getUserInfo(userName, userRole);
  }
}
