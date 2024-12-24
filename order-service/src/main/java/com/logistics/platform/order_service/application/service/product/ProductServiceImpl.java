package com.logistics.platform.order_service.application.service.product;

import com.logistics.platform.order_service.application.dto.ProductResponseDto;
import com.logistics.platform.order_service.infrastructure.client.ProductClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductClient productClient;

  @Override
  public ProductResponseDto getProductDtoByProductId(UUID productId, String userName,
      String userRole) {
    return productClient.getProductDtoByProductId(productId, userName, userRole);
  }

  @Override
  public void adjustProductQuantity(UUID productId, Long productQuantity) {
    productClient.adjustProductQuantity(productId, productQuantity);
  }
}
