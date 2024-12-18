package com.logistics.platform.product_service.application.service.auth;

import com.logistics.platform.product_service.application.dto.UserResDto;
import com.logistics.platform.product_service.infrastructure.client.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthClient authClient;

  @Override
  public UserResDto getUserInfo(String userName, String userRole) {
    return authClient.getUserInfo(userName, userRole);
  }

  @Override
  public UserResDto getUserInfoById(Long userId, String userName, String userRole) {
    return authClient.getUSerInfoById(userId, userName, userRole);
  }
}
