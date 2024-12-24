package com.logistics.platform.delivery_service.delivery.application.service;

import com.logistics.platform.delivery_service.delivery.application.dto.SlackRequestDto;
import com.logistics.platform.delivery_service.delivery.infrastructure.client.SlackClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackService {

  private final SlackClient slackClient;

  public void sendMessage(String deliveryManagerId, UUID startHubId, UUID endHubId, Double estimatedDistance, Double estimatedDuration, String userName, String userRole) {
    slackClient.sendSlackMessage(new SlackRequestDto(deliveryManagerId, startHubId, endHubId, estimatedDuration, estimatedDistance), userName, userRole);
  }
}
