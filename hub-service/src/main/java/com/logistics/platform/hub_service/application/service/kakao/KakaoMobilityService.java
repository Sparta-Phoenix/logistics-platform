package com.logistics.platform.hub_service.application.service.kakao;

import com.logistics.platform.hub_service.application.service.kakao.KakaoDirectionParser.DirectionInfo;
import com.logistics.platform.hub_service.presentation.global.ex.CustomApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoMobilityService {

  private final String API_URL = "https://apis-navi.kakaomobility.com/v1/directions";
  private final RestTemplate restTemplate;
  private final KakaoDirectionParser kakaoDirectionParser;

  private String kakaoApiKey;

  @Value("${kakao.api.key}")
  public void setKakaoApiKey(String kakaoApiKey) {
    this.kakaoApiKey = kakaoApiKey;
  }

  public List<String> getDirections(String start, String end) {
    String url = UriComponentsBuilder.fromHttpUrl(API_URL)
        .queryParam("origin", start)
        .queryParam("destination", end)
        .encode()
        .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK " + kakaoApiKey);

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        entity,
        String.class
    );

    if (response.getStatusCode() == HttpStatus.OK) {
      String responseBody = response.getBody();

      DirectionInfo directionInfo = kakaoDirectionParser.parseDirection(responseBody);

      return List.of(String.valueOf(directionInfo.getDuration()),
          String.valueOf(directionInfo.getDistance()));

    } else {
      throw new CustomApiException("경로를 찾는데 실패했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }
}
