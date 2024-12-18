package com.logistics.platform.order_service.presentation.global.exception;

public class CustomApiException extends RuntimeException{
  public CustomApiException(String message) {
    super(message);
  }
}