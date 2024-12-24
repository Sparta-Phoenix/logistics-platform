package com.logistics.platform.hub_service.application.service;

import com.logistics.platform.hub_service.application.service.kakao.KakaoMobilityService;
import com.logistics.platform.hub_service.domain.model.Hub;
import com.logistics.platform.hub_service.domain.model.HubRoute;
import com.logistics.platform.hub_service.domain.repository.HubRepository;
import com.logistics.platform.hub_service.domain.repository.HubRouteRepository;
import com.logistics.platform.hub_service.presentation.global.ex.CustomApiException;
import com.logistics.platform.hub_service.presentation.request.HubRouteCreateRequest;
import com.logistics.platform.hub_service.presentation.response.HubRouteCreateResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubRouteService {

  private final HubRouteRepository hubRouteRepository;
  private final KakaoMobilityService kakaoMobilityService;
  private final HubRepository hubRepository;

  @Transactional
  public List<HubRouteCreateResponse> createHubRoute(HubRouteCreateRequest hubRouteCreateRequest,
      String role, String userName) {
    if (role.equals("ROLE_MASTER")) {
    } else {
      throw new CustomApiException("권한이 없습니다.");
    }

    Hub startHub = hubRepository.findByHubIdAndIsDeletedFalse(hubRouteCreateRequest.getStartHubId())
        .orElseThrow(
            () -> new CustomApiException("해당 hubId가 존재하지 않습니다."));
    Hub endHub = hubRepository.findByHubIdAndIsDeletedFalse(hubRouteCreateRequest.getEndHubId())
        .orElseThrow(
            () -> new CustomApiException("해당 hubId가 존재하지 않습니다."));

    // 경도, 위도로 출발허브 - 중앙허브 - 도착허브 를 거치는 거리, 시간 구하기
    // 1) 출발 허브 id와 출발 허브의 중앙허브 우편번호로 중앙허브 찾아오기
    Hub centralHub = hubRepository.findByPostalCodeAndIsDeletedFalse(startHub.getCenterPostalCode())
        .orElseThrow(
            () -> new CustomApiException("해당 우편번호를 가진 중앙허브가 존재하지 않습니다."));
    // 2) 출발 허브 - 중앙허브, 중앙허브 - 도착허브 거리 계산 후 합산해서 저장
    List<HubRouteCreateResponse> savedHubRouteList = new ArrayList<>();

    // 출발 허브 - 중앙 허브 경로 계산
    List<String> firstDirections = kakaoMobilityService.getDirections(
        startHub.getLongitude() + "," + startHub.getLatitude(),
        centralHub.getLongitude() + "," + centralHub.getLatitude());
    // 중앙 허브 - 도착 허브 경로 계산
    List<String> secondDirections = kakaoMobilityService.getDirections(
        centralHub.getLongitude() + "," + centralHub.getLatitude(),
        endHub.getLongitude() + "," + endHub.getLatitude());

    HubRoute hubRoute1 = HubRoute.builder()
        .startHubId(startHub.getHubId())
        .endHubId(centralHub.getHubId())
        .estimatedDuration(Double.parseDouble(firstDirections.get(0)))
        .estimatedDistance(Double.parseDouble(firstDirections.get(1)))
        .createdBy(userName)
        .isDeleted(false)
        .build();

    HubRoute hubRoute2 = HubRoute.builder()
        .startHubId(centralHub.getHubId())
        .endHubId(endHub.getHubId())
        .estimatedDuration(Double.parseDouble(secondDirections.get(0)))
        .estimatedDistance(Double.parseDouble(secondDirections.get(1)))
        .createdBy(userName)
        .isDeleted(false)
        .build();

    HubRoute savedHubRoute1 = hubRouteRepository.save(hubRoute1);
    HubRoute savedHubRoute2 = hubRouteRepository.save(hubRoute2);


    savedHubRouteList.add(new HubRouteCreateResponse(savedHubRoute1));
    savedHubRouteList.add(new HubRouteCreateResponse(savedHubRoute2));

    return savedHubRouteList;
  }
}
