package com.logistics.platform.product_service.presentation.controller;

import com.logistics.platform.product_service.application.service.ProductService;
import com.logistics.platform.product_service.domain.model.Product;
import com.logistics.platform.product_service.presentation.request.ProductRequestDto;
import com.logistics.platform.product_service.presentation.response.ProductResponseDto;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {

    ProductResponseDto productResponseDto = productService.createProduct(productRequestDto);

    return ResponseEntity.ok().body(productResponseDto);
  }

  @GetMapping("/{productId}")
  public ResponseEntity getProduct(@PathVariable UUID productId) {

    ProductResponseDto productResponseDto = productService.getProduct(productId);

    return ResponseEntity.ok().body(productResponseDto);
  }

  @GetMapping
  public ResponseEntity getProductsPage(
      @RequestParam(required = false) List<UUID> uuidList,
      @QuerydslPredicate(root = Product.class) Predicate predicate,
      @PageableDefault(direction = Direction.DESC, sort = "createdAt") Pageable pageable) {

    PagedModel<ProductResponseDto> productResponseDtoPage
        = productService.getProductsPage(uuidList, predicate, pageable);

    return ResponseEntity.ok().body(productResponseDtoPage);
  }

  // 수정자 추가 예정
  @PatchMapping("/{productId}")
  public ResponseEntity updateProduct(
      @PathVariable UUID productId,
      @Valid @RequestBody ProductRequestDto productRequestDto) {

    ProductResponseDto productResponseDto
        = productService.updateProduct(productId, productRequestDto);

    return ResponseEntity.ok().body(productResponseDto);
  }

  // 삭제자 추가 예정
  @DeleteMapping("/{productId}")
  public ResponseEntity deleteProduct(
      @PathVariable UUID productId
  ) {
    ProductResponseDto productResponseDto = productService.deleteProduct(productId);

    return ResponseEntity.ok().body(productResponseDto.getProductName() + " 삭제 완료.");
  }

}
