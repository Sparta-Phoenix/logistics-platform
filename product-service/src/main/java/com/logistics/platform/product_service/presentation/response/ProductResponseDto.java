package com.logistics.platform.product_service.presentation.response;

import com.logistics.platform.product_service.domain.model.Product;
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

  public ProductResponseDto(Product product) {
    this.productId = product.getId();
    this.companyId = product.getCompanyId();
    this.hubId = product.getHubId();
    this.productName = product.getProductName();
    this.price = product.getPrice();
    this.count = product.getCount();
  }
}
