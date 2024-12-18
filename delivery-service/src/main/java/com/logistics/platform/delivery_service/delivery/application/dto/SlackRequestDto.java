package com.logistics.platform.delivery_service.delivery.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlackRequestDto {

  private String deliveryManagerSlackId;

  private UUID startHubId;

  private UUID endHubId;

  private Double estimatedDuration;

  private Double estimatedDistance;

  private String content;

  public SlackRequestDto(String deliveryManagerSlackId, UUID startHubId, UUID endHubId, Double estimatedDuration, Double estimatedDistance) {
    this.deliveryManagerSlackId = deliveryManagerSlackId;
    this.startHubId = startHubId;
    this.endHubId = endHubId;
    this.estimatedDuration = estimatedDuration;
    this.estimatedDistance = estimatedDistance;
  }
}
