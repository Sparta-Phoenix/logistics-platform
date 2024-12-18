package com.logistics.platform.hub_service.presentation.response;

import com.logistics.platform.hub_service.domain.model.HubRoute;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class HubRouteCreateResponse {

  private UUID hubRouteId;

  private UUID startHubId;

  private UUID endHubId;

  private Double estimatedDuration;

  private Double estimatedDistance;

  public HubRouteCreateResponse(HubRoute hubRoute) {
    this.hubRouteId = hubRoute.getHubRouteId();
    this.startHubId = hubRoute.getStartHubId();
    this.endHubId = hubRoute.getEndHubId();
    this.estimatedDuration = hubRoute.getEstimatedDuration();
    this.estimatedDistance = hubRoute.getEstimatedDistance();
  }
}
