package com.logistics.platform.order_service.application.service.product;

import com.logistics.platform.order_service.application.dto.ProductResponseDto;
import java.util.UUID;

public interface ProductService {

  ProductResponseDto getProductDtoByProductId(UUID productId, String userName, String userRole);

  void adjustProductQuantity(UUID productId, Long productQuantity);
}
