package com.logistics.platform.slack_service.presentation.controller;

import com.logistics.platform.slack_service.application.service.ai.AiService;
import com.logistics.platform.slack_service.common.ResponseDto;
import com.logistics.platform.slack_service.presentation.request.AiCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slacks/ai")
public class AiController {

  private final AiService aiService;

  @PostMapping
  public ResponseDto<String> createAi(@RequestBody AiCreateRequest request) throws Exception {
    String aiCreateResponse = aiService.createAi(request);
    return new ResponseDto<>(ResponseDto.SUCCESS, "AI 응답 생성이 완료되었습니다.", aiCreateResponse);
  }
}
