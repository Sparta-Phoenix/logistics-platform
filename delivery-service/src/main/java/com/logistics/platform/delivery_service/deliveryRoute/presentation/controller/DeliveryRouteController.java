package com.logistics.platform.delivery_service.deliveryRoute.presentation.controller;


import com.logistics.platform.delivery_service.deliveryRoute.application.service.DeliveryRouteService;
import com.logistics.platform.delivery_service.deliveryRoute.presentation.request.DeliveryRouteRequestDto;
import com.logistics.platform.delivery_service.deliveryRoute.presentation.response.DeliveryRouteResponseDto;
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
@RequestMapping("/api/delivery-routes")
public class DeliveryRouteController {

  private final DeliveryRouteService deliveryRouteService;

  /* 베송생성시 함께 생성됨 그래서 필요한가..? -> 필요!
  // 배송 경로 생성
  @PostMapping
  public ResponseDto<DeliveryRouteResponseDto> createDeliveryRoute(
      @RequestBody DeliveryRouteRequestDto deliveryRouteRequestDto) {
    DeliveryRouteResponseDto response = deliveryRouteService.createDeliveryRoutes(deliveryRouteRequestDto);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송 경로가 생성되었습니다.", response);
  }
  **/

  // 배송 경로 단건 조회
  @GetMapping("/{deliveryRouteId}")
  public ResponseDto<DeliveryRouteResponseDto> getDeliveryRoute(@PathVariable UUID deliveryRouteId) {
    DeliveryRouteResponseDto response = deliveryRouteService.getDeliveryRoute(deliveryRouteId);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송 경로가 조회되었습니다.", response);
  }

  // 배송 경로 목록 조회
  @GetMapping
  public ResponseDto<PagedModel<DeliveryRouteResponseDto>> getDeliveryRoutes(
      @RequestParam(required = false) List<UUID> uuidList,
      @RequestParam(required = false) Predicate predicate,
      Pageable pageable) {
    PagedModel<DeliveryRouteResponseDto> response = deliveryRouteService.getDeliveryRoutes(uuidList, predicate, pageable);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송 경로 목록이 조회되었습니다.", response);
  }

  // 배송 경로 수정
  @PatchMapping("/{deliveryRouteId}")
  public ResponseDto<DeliveryRouteResponseDto> updateDeliveryRoute(
      @PathVariable UUID deliveryRouteId,
      @RequestBody DeliveryRouteRequestDto deliveryRouteRequestDto) {
    DeliveryRouteResponseDto response = deliveryRouteService.updateDeliveryRoute(deliveryRouteId, deliveryRouteRequestDto);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송 경로가 수정되었습니다.", response);
  }

  // 배송 경로 삭제
  @DeleteMapping("/{deliveryRouteId}")
  public ResponseDto<DeliveryRouteResponseDto> deleteDeliveryRoute(
      @PathVariable UUID deliveryRouteId, @RequestHeader("deletedBy") String deletedBy) {
    DeliveryRouteResponseDto response = deliveryRouteService.deleteDeliveryRoute(deliveryRouteId, deletedBy);
    return new ResponseDto<>(ResponseDto.SUCCESS, "배송 경로가 삭제되었습니다.", response);
  }

}
