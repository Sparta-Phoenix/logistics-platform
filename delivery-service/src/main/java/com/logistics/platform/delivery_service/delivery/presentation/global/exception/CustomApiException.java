package com.logistics.platform.delivery_service.delivery.presentation.global.exception;

public class CustomApiException extends RuntimeException{
  public CustomApiException(String message) {
    super(message);
  }

}
