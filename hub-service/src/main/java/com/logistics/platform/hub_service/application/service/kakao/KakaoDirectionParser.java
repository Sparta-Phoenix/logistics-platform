package com.logistics.platform.hub_service.application.service.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class KakaoDirectionParser {

  public DirectionInfo parseDirection(String responseBody) {
    JSONObject jsonResponse = new JSONObject(responseBody);

    // routes 배열에서 summary 객체를 추출
    JSONObject summary = jsonResponse.getJSONArray("routes")
        .getJSONObject(0) // 제일 첫 번째 요소 사용해야 함.
        .getJSONObject("summary");

    double distance = Double.parseDouble(
        String.format("%.2f", summary.getInt("distance") / 1000.0)); // km
    double duration = Double.parseDouble(
        String.format("%.2f", summary.getInt("duration") / 3600.0)); // hour

    // DirectionInfo 객체에 저장하여 반환
    return new DirectionInfo(distance, duration);
  }

  // 거리와 시간 정보를 담을 클래스
  @Getter
  @AllArgsConstructor
  public static class DirectionInfo {

    private final double distance;
    private final double duration;

  }
}
