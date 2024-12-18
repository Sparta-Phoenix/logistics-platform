package com.logistics.platform.slack_service.common.exception;

public class CustomApiException extends RuntimeException {

  public CustomApiException(String message) {
    super(message);
  }
}

