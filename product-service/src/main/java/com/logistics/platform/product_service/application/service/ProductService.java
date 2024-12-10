package com.logistics.platform.product_service.application.service;

import com.logistics.platform.product_service.domain.model.Product;
import com.logistics.platform.product_service.domain.repository.ProductRepository;
import com.logistics.platform.product_service.presentation.request.ProductRequestDto;
import com.logistics.platform.product_service.presentation.response.ProductResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;


  public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {

    // companyId 검증

    // hubId 검증

    // 상품명 중복 가능

    // createdBy 추가 방법 의논 (gateway에서 인증 후 헤더로 값 전달?)
    Product product = Product.builder()
        .productName(productRequestDto.getProductName())
        .price(productRequestDto.getPrice())
        .count(productRequestDto.getCount())
        .createdBy("생성자")
        .companyId(productRequestDto.getCompanyId())
        .hubId(productRequestDto.getHubId())
        .build();

    return new ProductResponseDto(product);
  }

  public ProductResponseDto getProduct(UUID productId) {

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 productId 입니다."));

    return new ProductResponseDto(product);
  }
}
