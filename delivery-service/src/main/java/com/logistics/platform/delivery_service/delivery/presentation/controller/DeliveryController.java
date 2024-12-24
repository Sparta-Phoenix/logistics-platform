package com.logistics.platform.delivery_service.delivery.presentation.controller;


import com.logistics.platform.delivery_service.delivery.application.service.DeliveryService;
import com.logistics.platform.delivery_service.delivery.presentation.request.DeliveryRequestDto;
import com.logistics.platform.delivery_service.delivery.presentation.request.DeliveryUpdateRequestDto;
import com.logistics.platform.delivery_service.delivery.presentation.response.DeliveryResponseDto;
import com.logistics.platform.delivery_service.global.global.ResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController {

  private final DeliveryService deliveryService;

  // 배송 생성
  @PostMapping
  public ResponseDto<DeliveryResponseDto> createDelivery(
      @RequestBody DeliveryRequestDto deliveryRequestDto,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
  ) {
    DeliveryResponseDto response = deliveryService.createDelivery(deliveryRequestDto, userName,
        userRole);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송이 생성되었습니다.", response);
  }

  // 배송 단건 조회
  @GetMapping("/{deliveryId}")
  public ResponseDto<DeliveryResponseDto> getDelivery(@PathVariable UUID deliveryId) {
    DeliveryResponseDto response = deliveryService.getDelivery(deliveryId);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송이 조회되었습니다.", response);
  }

  // 배송 목록 조회
  @GetMapping
  public ResponseDto<PagedModel<DeliveryResponseDto>> getDeliveries(
      @RequestParam(required = false) List<UUID> uuidList,
      @RequestParam(required = false) Predicate predicate,
      Pageable pageable) {
    PagedModel<DeliveryResponseDto> response = deliveryService.getDeliveries(uuidList, predicate,
        pageable);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송 목록이 조회되었습니다.", response);
  }

  // 배송 수정
  @PatchMapping("/{deliveryId}")
  public ResponseDto<DeliveryResponseDto> updateDelivery(
      @PathVariable UUID deliveryId,
      @RequestBody DeliveryUpdateRequestDto deliveryUpdateRequestDto) {
    DeliveryResponseDto response = deliveryService.updateDelivery(deliveryId,
        deliveryUpdateRequestDto);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송이 수정되었습니다.", response);
  }

  // 배송 삭제
  @DeleteMapping("/{deliveryId}")
  public ResponseDto<DeliveryResponseDto> deleteDelivery(
      @PathVariable UUID deliveryId, @RequestHeader("deletedBy") String deletedBy) {
    DeliveryResponseDto response = deliveryService.deleteDelivery(deliveryId, deletedBy);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송이 삭제되었습니다.", response);
  }

  // 배송 취소
  @PostMapping("/{deliveryId}/cancel")
  public ResponseDto<DeliveryResponseDto> cancelDelivery(
      @PathVariable UUID deliveryId, @RequestHeader("cancelledBy") String cancelledBy) {
    DeliveryResponseDto response = deliveryService.cancelDelivery(deliveryId, cancelledBy);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송이 취소되었습니다.", response);
  }

}
