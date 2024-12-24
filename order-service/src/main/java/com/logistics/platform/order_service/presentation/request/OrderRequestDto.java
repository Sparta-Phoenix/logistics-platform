package com.logistics.platform.order_service.presentation.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestDto {

  @NotNull(message = "Product ID cannot be null")
  private UUID productId;

  @NotNull(message = "Product quantity cannot be null")
  private Long productQuantity;

  @NotNull(message = "supply company ID cannot be null")
  private UUID supplyCompanyId;

  @NotNull(message = "Receive company ID cannot be null")
  private UUID receiveCompanyId;

  @Size(max = 255, message = "Order request must not exceed 255 characters")
  private String orderRequest;

  private String address;

}
