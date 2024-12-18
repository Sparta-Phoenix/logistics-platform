package com.logistics.platform.auth_service.presentation.global.exception;

public class CustomApiException extends RuntimeException{
    public CustomApiException(String message) {
        super(message);
    }
}

