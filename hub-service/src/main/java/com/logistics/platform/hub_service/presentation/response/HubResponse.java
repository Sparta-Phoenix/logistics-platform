package com.logistics.platform.hub_service.presentation.response;

import com.logistics.platform.hub_service.domain.model.Hub;
import com.logistics.platform.hub_service.domain.model.HubType;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class HubResponse implements Serializable {

  private UUID hubId;

  private Long hubManagerId;

  private String hubName;

  private HubType hubType;

  private String centerPostalCode;

  private String roadAddress;

  private String postalCode;

  private Double latitude;

  private Double longitude;


  public HubResponse(Hub hub) {
    this.hubId = hub.getHubId();
    this.hubManagerId = hub.getHubManagerId();
    this.hubName = hub.getHubName();
    this.hubType = hub.getHubType();
    this.centerPostalCode = hub.getCenterPostalCode();
    this.roadAddress = hub.getRoadAddress();
    this.postalCode = hub.getPostalCode();
    this.latitude = hub.getLatitude();
    this.longitude = hub.getLongitude();
  }
}
