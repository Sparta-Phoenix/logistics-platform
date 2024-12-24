package com.logistics.platform.delivery_service.delivery.presentation.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.logistics.platform.delivery_service.delivery.domain.model.DeliveryStatus;
import jakarta.persistence.Column;
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
public class DeliveryRequestDto {

  private Long userId;
  private UUID startHubId;
  private UUID endHubId;
  private UUID orderId;
  private String recipient;
  private String recipientSlackId;
  private String address;

}
