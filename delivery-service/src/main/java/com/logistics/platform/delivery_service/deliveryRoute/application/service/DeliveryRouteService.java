package com.logistics.platform.delivery_service.deliveryRoute.application.service;


import com.logistics.platform.delivery_service.delivery.application.dto.HubRouteCreateRequest;
import com.logistics.platform.delivery_service.delivery.application.dto.HubRouteResponseDto;
import com.logistics.platform.delivery_service.delivery.domain.model.Delivery;
import com.logistics.platform.delivery_service.delivery.domain.repository.DeliveryRepository;
import com.logistics.platform.delivery_service.delivery.infrastructure.client.HubClient;
import com.logistics.platform.delivery_service.deliveryRoute.application.service.dto.DeliveryManagerResponseDto;
import com.logistics.platform.delivery_service.deliveryRoute.domain.model.DeliveryRoute;
import com.logistics.platform.delivery_service.deliveryRoute.domain.model.DeliveryRouteStatus;
import com.logistics.platform.delivery_service.deliveryRoute.domain.repository.DeliveryRouteRepository;
import com.logistics.platform.delivery_service.deliveryRoute.infrastructure.client.DeliveryManagerClient;
import com.logistics.platform.delivery_service.deliveryRoute.presentation.request.DeliveryRouteRequestDto;
import com.logistics.platform.delivery_service.deliveryRoute.presentation.response.DeliveryRouteResponseDto;
import com.logistics.platform.delivery_service.global.global.exception.CustomApiException;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryRouteService {

  private final DeliveryRouteRepository deliveryRouteRepository;
  private final DeliveryManagerClient deliveryManagerClient;
  private final DeliveryRepository deliveryRepository;
  private final HubClient hubClient;

  // 1. 배송 경로 생성
  @Transactional
  public List<DeliveryRouteResponseDto> createDeliveryRoutes(Delivery savedDelivery,
      String userName, String userRole) {
    List<HubRouteResponseDto> hubRoutes = hubClient.getHubRoutes(
        new HubRouteCreateRequest(savedDelivery.getStartHubId(), savedDelivery.getEndHubId()),
        userName,
        userRole
    );

    Long sequence = 0L;

    List<DeliveryRouteResponseDto> deliveryRouteResponseDtoList = new ArrayList<>();

    for (HubRouteResponseDto hubRoute : hubRoutes) {
      DeliveryManagerResponseDto deliveryManager =
          deliveryManagerClient.getNextAvailableDeliveryManager();

      if (deliveryManager == null || deliveryManager.getDeliveryManagerId() == null) {
        throw new CustomApiException("배정 가능한 배송담당자가 없습니다.");
      }

      DeliveryRoute deliveryRoute = DeliveryRoute.builder()
          .delivery(savedDelivery)
          .startHubId(hubRoute.getStartHubId())
          .endHubId(hubRoute.getEndHubId())
          .estimatedDuration(hubRoute.getEstimatedDuration())  // 소요 시간 반영
          .estimatedDistance(hubRoute.getEstimatedDistance())  // 거리 반영
          .deliveryManagerId(deliveryManager.getDeliveryManagerId())  // 담당자 ID 설정
          .sequence(sequence++)
          .status(DeliveryRouteStatus.WAITING_AT_HUB)
          .createdBy(userName)
          .isDeleted(false)
          .build();

      savedDelivery.addDeliveryRoute(deliveryRoute);  // 양방향 매핑 설정

      deliveryRouteResponseDtoList.add(
          new DeliveryRouteResponseDto(deliveryRoute, deliveryManager.getDeliveryManagerSlackId()));
      // 저장
      deliveryRouteRepository.save(deliveryRoute);
    }

    return deliveryRouteResponseDtoList; // 생성된 경로 반환 // deli
  }


  // 2. 배송 경로 단건 조회
  @Transactional(readOnly = true)
  public DeliveryRouteResponseDto getDeliveryRoute(UUID deliveryRouteId) {
    DeliveryRoute deliveryRoute = deliveryRouteRepository.findById(deliveryRouteId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 배송 경로 ID입니다."));

    if (deliveryRoute.getStatus() == DeliveryRouteStatus.DELETED) {
      throw new CustomApiException("이미 삭제된 배송 경로입니다.");
    }

    return new DeliveryRouteResponseDto(deliveryRoute);
  }

  // 3. 배송 경로 목록 조회
  @Transactional(readOnly = true)
  public PagedModel<DeliveryRouteResponseDto> getDeliveryRoutes(List<UUID> uuidList,
      Predicate predicate, Pageable pageable) {
    Page<DeliveryRouteResponseDto> deliveryRoutePage = deliveryRouteRepository.findAll(uuidList,
        predicate, pageable);
    return new PagedModel<>(deliveryRoutePage);
  }

  // 4. 배송 경로 수정
  @Transactional
  public DeliveryRouteResponseDto updateDeliveryRoute(UUID deliveryRouteId,
      DeliveryRouteRequestDto deliveryRouteRequestDto) {
    DeliveryRoute deliveryRoute = deliveryRouteRepository.findById(deliveryRouteId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 배송 경로 ID입니다."));

    if (deliveryRoute.getStatus() == DeliveryRouteStatus.DELETED) {
      throw new CustomApiException("이미 삭제된 배송 경로입니다.");
    }

    deliveryRoute.updateActualMetrics(deliveryRouteRequestDto.getEstimatedDuration(),
        deliveryRouteRequestDto.getEstimatedDistance());
    deliveryRoute.updateStatus(deliveryRouteRequestDto.getStatus());
    deliveryRouteRepository.save(deliveryRoute);
    return new DeliveryRouteResponseDto(deliveryRoute);
  }

  // 5. 배송 경로 삭제
  @Transactional
  public DeliveryRouteResponseDto deleteDeliveryRoute(UUID deliveryRouteId, String deletedBy) {
    DeliveryRoute deliveryRoute = deliveryRouteRepository.findById(deliveryRouteId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 배송 경로 ID입니다."));

    if (deliveryRoute.getStatus() == DeliveryRouteStatus.DELETED) {
      throw new CustomApiException("이미 삭제된 배송 경로입니다.");
    }

    deliveryRoute.deleteDeliveryRoute(deletedBy);
    return new DeliveryRouteResponseDto(deliveryRoute);
  }


}

