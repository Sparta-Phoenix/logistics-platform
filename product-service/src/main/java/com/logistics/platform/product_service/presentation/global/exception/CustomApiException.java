package com.logistics.platform.product_service.presentation.global.exception;

public class CustomApiException extends RuntimeException{
  public CustomApiException(String message) {
    super(message);
  }
}