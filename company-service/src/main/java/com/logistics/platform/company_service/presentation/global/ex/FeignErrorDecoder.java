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

    // 에러의 msg 부분을 가져와 json 형태로 만들어줌
    String errorMessage = null;
    try {
      errorMessage = objectMapper.readTree(body).path("msg").asText();
      if (errorMessage.isEmpty()) {
        errorMessage = "에러 메시지가 없습니다.";
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException("JSON 파싱 중 문제가 발생했습니다.", e);
    }

    return new CustomApiException(errorMessage);
  }
}
