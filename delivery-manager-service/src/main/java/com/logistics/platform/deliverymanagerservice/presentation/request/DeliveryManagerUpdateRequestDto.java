package com.logistics.platform.deliverymanagerservice.presentation.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryType;
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
public class DeliveryManagerUpdateRequestDto {

  private Long userId;
  private UUID hubId;
  private String slackId;
  private DeliveryType deliveryType;
  private Long deliveryOrderNumber;

}
