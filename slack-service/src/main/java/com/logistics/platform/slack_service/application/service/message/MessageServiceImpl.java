package com.logistics.platform.slack_service.application.service.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.platform.slack_service.common.exception.CustomApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageServiceImpl implements MessageService {

  @Value("${slack.bot.token}")
  private String botToken;

  private final RestTemplate restTemplate;

  public MessageServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public String sendMessageToUser(String userSlackId, String message) {
    String url = "https://slack.com/api/chat.postMessage";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(botToken);

    String payload = String.format(
        "{\"channel\": \"%s\", \"text\": \"%s\"}",
        userSlackId,
        message
    );

    HttpEntity<String> request = new HttpEntity<>(payload, headers);
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
        String.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new CustomApiException("Failed to send message: " + response.getBody());
    }

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      try {
        // JSON 파싱하여 ts 값 가져오기
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        String ts = responseJson.get("ts").asText();
        return ts; // 메시지의 타임스탬프 반환
      } catch (JsonProcessingException e) {
        throw new CustomApiException("Failed to parse Slack API response: " + e);
      }
    } else {
      throw new CustomApiException("Failed to send message: " + response.getBody());
    }
  }

  @Override
  public void updateSendMessage(String channel, String ts, String newText) {
    String url = "https://slack.com/api/chat.update";

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    headers.setBearerAuth(botToken);

    String payload = String.format(
        "{\"channel\": \"%s\", \"ts\": \"%s\", \"text\": \"%s\"}",
        channel, ts, newText
    );

    HttpEntity<String> request = new HttpEntity<>(payload, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
        String.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new CustomApiException("Failed to update message: " + response.getBody());
    }
  }

  public void deleteSendMessage(String channel, String ts) {
    String url = "https://slack.com/api/chat.delete";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(botToken);

    String payload = String.format(
        "{\"channel\": \"%s\", \"ts\": \"%s\"}",
        channel, ts
    );

    HttpEntity<String> request = new HttpEntity<>(payload, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
        String.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new CustomApiException("Failed to delete message: " + response.getBody());
    }
  }

  // Email로 SlackID 가져오는 코드
//  public String getUserSlackIdByEmail(String email) {
//    String url = "https://slack.com/api/users.lookupByEmail";
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setBearerAuth(botToken);
//
//    HttpEntity<String> request = new HttpEntity<>(headers);
//    String apiUrl = String.format("%s?email=%s", url, email);
//
//    ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, request, String.class);
//
//    // Parse response to extract user ID
//    if (response.getStatusCode().is2xxSuccessful()) {
//      // JSON Parsing logic to extract "user.id" from response
//    } else {
//      throw new RuntimeException("Failed to fetch user ID for email: " + email);
//    }
//  }

}

