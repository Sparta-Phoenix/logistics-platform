package com.logistics.platform.company_service.application.service;

import com.logistics.platform.company_service.application.dto.UserResDto;
import com.logistics.platform.company_service.infrastructure.client.UserClient;
import com.logistics.platform.company_service.presentation.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserClient userClient;

  @Override
  @Retryable(value = CustomApiException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000)) // 1초 간격으로 최대 3번 재시도
  public UserResDto getUserId(
      @RequestHeader(value = "X-User-Role") String role,
      @RequestHeader(value = "X-User-Name") String userName) {
    return userClient.getUserId(role, userName);
  }

}
