package com.logistics.platform.slack_service.application.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.platform.slack_service.presentation.request.AiCreateRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiService {

  @Value("${google.gemini.api.key}")
  private String googleGeminiApiKey;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public String createAi(AiCreateRequest request) throws Exception {
    String prompt = createPrompt(request);

    String apiUrl =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
            + googleGeminiApiKey;

    // HTTP Client 생성 및 요청 처리
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      String requestBody = String.format("""
              {
                  "contents": [ {
                      "parts": [{"text": "%s"}]
                  } ],
                  "safetySettings": [ {
                      "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
                      "threshold": "BLOCK_ONLY_HIGH"
                  } ],
                  "generationConfig": {
                      "stopSequences": ["Title"],
                      "temperature": 1.0,
                      "maxOutputTokens": 800,
                      "topP": 0.8,
                      "topK": 10
                  }
              }
          """, prompt);

      HttpPost httpPost = new HttpPost(apiUrl);
      httpPost.setHeader("Content-Type", "application/json");
      httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));

      // HTTP 요청 및 응답 처리
      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)
        );
        StringBuilder responseBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          responseBody.append(line);
        }

        // JSON 파싱
        JsonNode jsonResponse = objectMapper.readTree(responseBody.toString());


        return jsonResponse.findPath("text").asText();
      }
    }
  }

  // 프롬프트 생성 메서드
  private String createPrompt(AiCreateRequest request) {
    return String.format(
        "User: %s\n" +
            "Product: %s\n" +
            "StartDate: %s\n" +
            "Start Hub Address: %s\n" +
            "Central Hub Address: %s\n" +
            "End Hub Address: %s\n" +
            "Based on the above information, %s",
        request.getUserName(),
        request.getProductName(),
        request.getStartDate(),
        request.getStartHubAddress(),
        request.getCentralHubAddress(),
        request.getEndHubAddress(),
        request.getPrompt()
    );
  }
}
