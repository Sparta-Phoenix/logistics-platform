package com.logistics.platform.deliverymanagerservice.application.service;

import com.logistics.platform.deliverymanagerservice.application.dto.UserResDto;
import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryManager;
import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryType;
import com.logistics.platform.deliverymanagerservice.domain.repository.DeliveryManagerRepository;
import com.logistics.platform.deliverymanagerservice.infrastructure.client.HubClient;
import com.logistics.platform.deliverymanagerservice.infrastructure.client.UserClient;
import com.logistics.platform.deliverymanagerservice.presentation.global.exception.CustomApiException;
import com.logistics.platform.deliverymanagerservice.presentation.request.DeliveryManagerRequestDto;
import com.logistics.platform.deliverymanagerservice.presentation.response.DeliveryManagerResponseDto;
import com.querydsl.core.types.Predicate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
public class DeliveryManagerService {

  private final DeliveryManagerRepository deliveryManagerRepository;
  private final HubClient hubClient;
  private final UserClient userClient;
  private final ManagerOrderIndexService managerOrderIndexService;
  private final DeliveryType deliveryType = DeliveryType.HUB;
  private final UserSerivce userSerivce;

  // 1. 배송담당자 생성
  @CircuitBreaker(name = "DeliveryManagerService", fallbackMethod = "handleDeliveryManagerFailure")
  public DeliveryManagerResponseDto createDeliveryManager(
      DeliveryManagerRequestDto deliveryManagerRequestDto,
      String userName, String userRole) {
    // 존재하는 User인지 확인
    UserResDto userResDto = userClient.getUserInfo(
        deliveryManagerRequestDto.getUserName(),
        userRole
    );

    if (userResDto == null) {
      throw new CustomApiException("유효하지 않은 유저입니다.");
    }

    // 이미 배송담당자로 등록되어있는 User인지 확인
    if (deliveryManagerRepository.existsByUserId(deliveryManagerRequestDto.getUserId())) {
      throw new CustomApiException("이미 등록된 배송담당자입니다.");
    }

    // 배송타입별 배송순번 로직
    DeliveryType deliveryType = deliveryManagerRequestDto.getDeliveryType();

    Long maxOrderNumber;

    if (deliveryType == DeliveryType.HUB) { // 허브 배송담당자 로직
      maxOrderNumber = deliveryManagerRepository.findMaxDeliveryOrderNumberByDeliveryType(
          DeliveryType.HUB).orElse(0L);
    } else if (deliveryType == DeliveryType.COMPANY) { //업체 배송담당자 로직
      // 존재하는 Hub인지 확인 -> 안해도?
      // 업체베송담당자는 소속허브 ID가 존재하는 ID인지 확인해야 한대서 일단 넣어봤습니당...
      boolean hubExists = hubClient.checkIfHubExists(deliveryManagerRequestDto.getHubId());
      if (!hubExists) {
        throw new CustomApiException("존재하지 않는 허브ID입니다.");
      }
      maxOrderNumber = deliveryManagerRepository.findMaxDeliveryOrderNumberByDeliveryType(
          DeliveryType.COMPANY).orElse(0L);
    } else {
      throw new CustomApiException("유효하지 않은 배송 타입입니다."); // 둘 중에 없으면 메세지 던지기
    }

    DeliveryManager savedDeliveryManager = DeliveryManager.builder()
        .userId(userResDto.getId())
        .hubId(deliveryManagerRequestDto.getHubId())
        .slackId(userResDto.getSlackId())
        .deliveryType(deliveryManagerRequestDto.getDeliveryType())
        .deliveryOrderNumber(maxOrderNumber + 1)
        .createdBy(userName)
        .isDeleted(false)
        .build();

    savedDeliveryManager = deliveryManagerRepository.save(savedDeliveryManager);

    return new DeliveryManagerResponseDto(savedDeliveryManager);
  }


  // 2. 배송담당자 수정
  @Transactional
  public DeliveryManagerResponseDto updateDeliveryManager(UUID deliveryManagerId,
      DeliveryManagerRequestDto deliveryManagerRequestDto) {

    DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryManagerId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 배송담당자ID입니다."));

    if (deliveryManager.getIsDeleted()) {
      throw new CustomApiException("이미 삭제된 배송담당자입니다.");
    }

    deliveryManager.updateDeliveryManager(deliveryManagerRequestDto);
    DeliveryManager savedDeliveryManager = deliveryManagerRepository.save(deliveryManager);

    return new DeliveryManagerResponseDto(savedDeliveryManager);

  }


  // 3. 배송담당자 단건 조회
  @Transactional(readOnly = true)
  public DeliveryManagerResponseDto getDeliveryManager(UUID deliveryManagerId) {

    DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryManagerId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 배송담당자ID 입니다."));

    if (deliveryManager.getIsDeleted()) {
      throw new CustomApiException("이미 삭제된 배송담당자입니다.");
    }

    return new DeliveryManagerResponseDto(deliveryManager);
  }


  // 4. 배송담당자 목록 조회
  @Transactional(readOnly = true)
  public PagedModel<DeliveryManagerResponseDto> getDeliveryManagersPage(
      List<UUID> uuidList, Predicate predicate, Pageable pageable) {

    Page<DeliveryManagerResponseDto> deliveryManagerResponseDtoPage
        = deliveryManagerRepository.findAll(uuidList, predicate, pageable);

    return new PagedModel<>(deliveryManagerResponseDtoPage);
  }


  // 5. 배송담당자 삭제
  @Transactional
  public DeliveryManagerResponseDto deleteDeliveryManager(UUID deliveryManagerId) {

    DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryManagerId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 배송담당자ID입니다."));

    if (deliveryManager.getIsDeleted()) {
      throw new CustomApiException("이미 삭제된 배송담당자입니다.");
    }

    deliveryManager.deleteDeliveryManager();  // 삭제자 나중에 헤더에서

    return new DeliveryManagerResponseDto(deliveryManager);
  }

/*
  // 배송타입 검증
  public void validateDeliveryType(DeliveryType deliveryType) {
    if (deliveryType == null) {
      throw new CustomApiException("배송 유형이 비어있습니다.");
    }
    if (deliveryType != DeliveryType.HUB && deliveryType != DeliveryType.COMPANY) {
      throw new CustomApiException("유효하지 않은 배송 유형입니다.");
    }
  }
   // 필요없을듯...?
 */

  // 6. 배송담당자 배정
  @Transactional
  public DeliveryManagerResponseDto getNextAvailableDeliveryManager() {

    // 현재 배송순번 인덱스를 시스템에서 관리하는 방식 적용
    Long currentOrderIndex = managerOrderIndexService.getCurrentOrderIndex(deliveryType);
    // 요건 다음 배송순번이 아닌 배송순번 1번째인 사람만 조회되는듯
    DeliveryManager nextManager = deliveryManagerRepository
        .findFirstByIsDeletedFalseAndDeliveryTypeAndDeliveryOrderNumber(deliveryType,
            currentOrderIndex)
        .orElseThrow(() -> new CustomApiException("배정 가능한 배송담당자가 없습니다."));

    if (nextManager == null) {
      throw new CustomApiException("배정 가능한 배송담당자가 없습니다.");
    }

    // 최소 및 최대 배송 순번 조회 -> 타입에 따라 다르게 되어있지 않음
    Long minOrderNumber = deliveryManagerRepository.findMinDeliveryOrderNumberByDeliveryType(
            deliveryType)
        .orElseThrow(() -> new CustomApiException("배송 순번 조회 오류"));
    Long maxOrderNumber = deliveryManagerRepository.findMaxDeliveryOrderNumberByDeliveryType(
            deliveryType)
        .orElseThrow(() -> new CustomApiException("배송 순번 조회 오류"));

    // 배송 순번 증가 및 순환 로직 -> 배송순번을 변경하는 것 보다 시스템에서 다음 호출할 배송순번을 기억하는 것이 좋을듯
    Long currentOrderNumber = nextManager.getDeliveryOrderNumber();
    Long nextOrderNumber =
        (currentOrderNumber >= maxOrderNumber) ? minOrderNumber : currentOrderNumber + 1;

    // 다음 배송 순번 인덱스 계산 및 업데이트
    Long nextOrderIndex =
        (currentOrderIndex >= maxOrderNumber) ? minOrderNumber : currentOrderIndex + 1;
    managerOrderIndexService.updateCurrentOrderIndex(deliveryType, nextOrderIndex);

    return new DeliveryManagerResponseDto(nextManager.getId(), nextManager.getSlackId());
  }


  public DeliveryManagerResponseDto handleDeliveryManagerFailure(
      DeliveryManagerRequestDto deliveryManagerRequestDto, Throwable t) {
    return new DeliveryManagerResponseDto(t.getMessage());
  }

}
