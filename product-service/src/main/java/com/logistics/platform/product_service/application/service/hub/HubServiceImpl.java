package com.logistics.platform.product_service.application.service.hub;

import com.logistics.platform.product_service.infrastructure.client.HubClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HunService {

  private final HubClient hubClient;

  @Override
  public UUID getHubIdByManagerId(Long id, String userName, String userRole) {
    return hubClient.getHubIdByManagerId(id, userName, userRole);
  }
}
