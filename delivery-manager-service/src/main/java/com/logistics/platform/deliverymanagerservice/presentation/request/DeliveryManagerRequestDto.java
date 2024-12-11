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
public class DeliveryManagerRequestDto {

  private Long userId;
  private UUID hubId;
  private UUID slackId;
  private DeliveryType deliveryType;
  private Long deliveryOrderNumber;
  //배송순번을 값을 받아올지, 아니면 generateNumber 이런걸로 엔터티에서 생성로직 추가해서 넣어줄지
}
