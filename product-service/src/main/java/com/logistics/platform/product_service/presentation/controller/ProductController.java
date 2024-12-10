package com.logistics.platform.product_service.presentation.controller;

import com.logistics.platform.product_service.application.service.ProductService;
import com.logistics.platform.product_service.presentation.request.ProductRequestDto;
import com.logistics.platform.product_service.presentation.response.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity createProduct(@RequestBody ProductRequestDto productRequestDto) {

    ProductResponseDto productResponseDto = productService.createProduct(productRequestDto);

    return ResponseEntity.ok().body(productResponseDto);
  }

}
