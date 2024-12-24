package com.logistics.platform.company_service.application.service;

import com.logistics.platform.company_service.application.dto.HubResponseDto;
import com.logistics.platform.company_service.infrastructure.client.HubClient;
import com.logistics.platform.company_service.presentation.global.ex.CustomApiException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {

  private final HubClient hubClient;
  @Override
  @Retryable(value = CustomApiException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000)) // 1초 간격으로 최대 3번 재시도
  @Cacheable(cacheNames = "getHubDtoByHubIdCache", key = "#hubId")
  public HubResponseDto getHubDtoByHubId(UUID hubId) {
    return hubClient.getHubResponseDto(hubId);
  }
}
