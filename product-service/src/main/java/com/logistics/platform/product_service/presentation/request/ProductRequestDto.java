package com.logistics.platform.product_service.presentation.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
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
public class ProductRequestDto {

  private UUID companyId;

  private UUID hubId;

  private String productName;

  @Min(value = 0, message = "Price cannot be negative")
  private Long price;

  @Min(value = 0, message = "Count cannot be negative")
  private Long count;
}
