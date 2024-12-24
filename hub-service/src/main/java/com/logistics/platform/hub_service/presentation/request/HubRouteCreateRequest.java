package com.logistics.platform.hub_service.presentation.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HubRouteCreateRequest {

  private UUID startHubId;

  private UUID endHubId;
}
