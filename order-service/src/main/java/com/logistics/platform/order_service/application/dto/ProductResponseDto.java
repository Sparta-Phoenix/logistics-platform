package com.logistics.platform.order_service.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

  private UUID productId;

  private UUID companyId;

  private UUID hubId;

  private String productName;

  private Long price;

  private Long count;

}