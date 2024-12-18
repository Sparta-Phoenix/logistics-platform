package com.logistics.platform.product_service.presentation.global.hadler;

import com.logistics.platform.product_service.presentation.global.ResponseDto;
import com.logistics.platform.product_service.presentation.global.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

  @ExceptionHandler(CustomApiException.class)
  public ResponseEntity<?> apiException(CustomApiException e) {
    log.error(e.getMessage());
    return new ResponseEntity<>(new ResponseDto<>(ResponseDto.FAILURE, e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseDto> handleValidationException(BindingResult bindingResult) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ResponseDto<>(ResponseDto.FAILURE,
            bindingResult.getAllErrors().get(0).getDefaultMessage(),
            HttpStatus.BAD_REQUEST));
  }

}