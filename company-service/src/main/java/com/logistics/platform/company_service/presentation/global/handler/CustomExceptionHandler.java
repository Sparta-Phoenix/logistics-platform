package com.logistics.platform.company_service.presentation.global.handler;

import com.logistics.platform.company_service.presentation.global.ResponseDto;
import com.logistics.platform.company_service.presentation.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {
  @ExceptionHandler(CustomApiException.class)
  public ResponseEntity<?> apiException(CustomApiException e) {
    log.error(e.getMessage());
    return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
  }
}
