package com.logistics.platform.product_service.presentation.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

  private UUID companyId;

  private UUID hubId;

  private String productName;

  private Long price;

  private Long count;
}
