package com.logistics.platform.slack_service.application.service.auth;

import com.logistics.platform.slack_service.infrastructure.client.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthClient authClient;

  @Override
  @Cacheable(value = "getSlackId", key = "#username")
  public String getSlackIdByUsername(String username) {
    return authClient.getSlackIdByUsername(username);
  }
}
