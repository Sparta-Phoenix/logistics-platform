package com.logistics.platform.company_service.presentation.global.ex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Exception decode(String s, Response response) {
    // 에러 메시지를 읽어옴
    String body = null;
    try {
      byte[] bodyBytes = response.body().asInputStream().readAllBytes();
      body = new String(bodyBytes, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("응답을 읽는 중 문제가 발생했습니다.", e);
    }

    String errorMessage;

    // JSON 형식인지 확인하고 처리
    if (isJson(body)) {
      try {
        errorMessage = objectMapper.readTree(body).path("msg").asText();
        if (errorMessage.isEmpty()) {
          errorMessage = "에러 메시지가 없습니다.";
        }
      } catch (JsonProcessingException e) {
        throw new RuntimeException("JSON 파싱 중 문제가 발생했습니다.", e);
      }
    } else {
      // JSON이 아닐 경우 body 전체를 에러 메시지로 반환
      errorMessage = body;
    }

    return new CustomApiException(errorMessage);
  }

  // JSON 유효성 검사 메서드
  private boolean isJson(String body) {
    try {
      objectMapper.readTree(body);
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}


