package com.logistics.platform.deliverymanagerservice.application.service;

import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryManager;
import com.logistics.platform.deliverymanagerservice.domain.repository.DeliveryManagerRepository;
import com.logistics.platform.deliverymanagerservice.presentation.request.DeliveryManagerRequestDto;
import com.logistics.platform.deliverymanagerservice.presentation.response.DeliveryManagerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

  private final DeliveryManagerRepository deliveryManagerRepository;

  public DeliveryManagerResponseDto createDeliveryManager(DeliveryManagerRequestDto deliveryManagerRequestDto) {
    //존재하는 User인지 확인
    // 이미 배송담당자로 등록되어있는 User인지 확인
    //존재하는 Hub인지 확인
    DeliveryManager savedDeliveryManager = DeliveryManager.builder()
        .userId(deliveryManagerRequestDto.getUserId())
        .hubId(deliveryManagerRequestDto.getHubId())
        .slackId(deliveryManagerRequestDto.getSlackId())
        .deliveryType(deliveryManagerRequestDto.getDeliveryType())
        .deliveryOrderNumber(deliveryManagerRequestDto.getDeliveryOrderNumber())
        .build();

    savedDeliveryManager = deliveryManagerRepository.save(savedDeliveryManager);

    return new DeliveryManagerResponseDto(savedDeliveryManager);
  }



}
