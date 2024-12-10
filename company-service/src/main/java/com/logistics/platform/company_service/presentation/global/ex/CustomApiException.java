package com.logistics.platform.company_service.presentation.global.ex;

public class CustomApiException extends RuntimeException{
  public CustomApiException(String message) {
    super(message);
  }
}
