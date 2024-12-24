package com.logistics.platform.deliverymanagerservice.application.service;

import com.logistics.platform.deliverymanagerservice.application.dto.UserResDto;
import com.logistics.platform.deliverymanagerservice.infrastructure.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserSerivce {

  private final UserClient userClient;

  @Override
  public UserResDto getUserInfo(String userName, String userRole) {
    return userClient.getUserInfo(userName, userRole);
  }
}
