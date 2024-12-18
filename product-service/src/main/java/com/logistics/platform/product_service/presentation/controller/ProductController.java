package com.logistics.platform.product_service.presentation.controller;

import com.logistics.platform.product_service.application.service.ProductService;
import com.logistics.platform.product_service.domain.model.Product;
import com.logistics.platform.product_service.presentation.global.ResponseDto;
import com.logistics.platform.product_service.presentation.request.ProductRequestDto;
import com.logistics.platform.product_service.presentation.response.ProductResponseDto;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseDto<ProductResponseDto> createProduct(
      @Valid @RequestBody ProductRequestDto productRequestDto,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole) {

    ProductResponseDto productResponseDto = productService.createProduct(productRequestDto,
        userName, userRole);

    return new ResponseDto<>(ResponseDto.SUCCESS, "상품이 생성되었습니다.", productResponseDto);
  }

  @GetMapping("/{productId}")
  public ResponseDto<ProductResponseDto> getProduct(
      @PathVariable UUID productId,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
  ) {

    ProductResponseDto productResponseDto = productService.getProduct(productId, userName,
        userRole);

    return new ResponseDto<>(ResponseDto.SUCCESS, "상품이 조회되었습니다.", productResponseDto);
  }

  @GetMapping
  public ResponseDto<PagedModel<ProductResponseDto>> getProductsPage(
      @RequestParam(required = false) List<UUID> uuidList,
      @QuerydslPredicate(root = Product.class) Predicate predicate,
      Pageable pageable,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
  ) {

    PagedModel<ProductResponseDto> productResponseDtoPage
        = productService.getProductsPage(uuidList, predicate, pageable, userName, userRole);

    return new ResponseDto<>(ResponseDto.SUCCESS, "상품이 목록이 조회되었습니다.", productResponseDtoPage);
  }

  @PatchMapping("/{productId}")
  public ResponseDto<ProductResponseDto> updateProduct(
      @PathVariable UUID productId,
      @Valid @RequestBody ProductRequestDto productRequestDto,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
  ) {

    ProductResponseDto productResponseDto
        = productService.updateProduct(productId, productRequestDto, userName, userRole);

    return new ResponseDto<>(ResponseDto.SUCCESS, "상품이 수정되었습니다.", productResponseDto);
  }

  @DeleteMapping("/{productId}")
  public ResponseDto deleteProduct(
      @PathVariable UUID productId,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
  ) {

    ProductResponseDto productResponseDto = productService.deleteProduct(productId, userName,
        userRole);

    return new ResponseDto<>(
        ResponseDto.SUCCESS,
        productResponseDto.getProductName() + ": 상품이 삭제되었습니다."
    );
  }

  @GetMapping("/{productId}/info")
  public ProductResponseDto getProductDto(@PathVariable UUID productId) {
    return productService.getProductWithOutValidateRole(productId);
  }

  @PostMapping("/{productId}/quantity/adjustment")
  public void adjustProductQuantity(
      @PathVariable(value = "productId") UUID productId,
      @RequestParam(value = "quantity") Long quantity) {

    productService.adjustProductQuantity(productId, quantity);
  }
}
