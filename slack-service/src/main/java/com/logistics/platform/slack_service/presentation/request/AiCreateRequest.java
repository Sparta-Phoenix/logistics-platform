package com.logistics.platform.slack_service.presentation.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiCreateRequest {

  private String userName;

  private String productName;

  private String startDate;

  private String startHubAddress;

  private String centralHubAddress;

  private String endHubAddress;

  private String prompt;



}
