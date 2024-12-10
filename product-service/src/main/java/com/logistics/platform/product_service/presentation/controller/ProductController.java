package com.logistics.platform.product_service.presentation.controller;

import com.logistics.platform.product_service.application.service.ProductService;
import com.logistics.platform.product_service.presentation.request.ProductRequestDto;
import com.logistics.platform.product_service.presentation.response.ProductResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/{productId}")
  public ResponseEntity getProduct(@PathVariable UUID productId) {

    ProductResponseDto productResponseDto = productService.getProduct(productId);

    return ResponseEntity.ok().body(productResponseDto);
  }

}
