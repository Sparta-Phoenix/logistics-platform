package com.logistics.platform.product_service.presentation.controller;

import com.logistics.platform.product_service.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class ProductController {

  private final ProductService productService;


}
