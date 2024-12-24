package com.logistics.platform.order_service.presentation.controller;

import com.logistics.platform.order_service.application.service.order.OrderService;
import com.logistics.platform.order_service.domain.model.Order;
import com.logistics.platform.order_service.presentation.global.ResponseDto;
import com.logistics.platform.order_service.presentation.request.OrderRequestDto;
import com.logistics.platform.order_service.presentation.response.OrderResponseDto;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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
@RequestMapping("/api/orders")
public class OrderController { // TODO 권한 검증, 실패시 처리 로직

  private final OrderService orderService;

  @PostMapping
  public ResponseDto<OrderResponseDto> createOrder(
      @Valid @RequestBody OrderRequestDto orderRequestDto,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
  ) {

    OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, userName,
        userRole);

    if (orderResponseDto.getMessage() == null) {
      return new ResponseDto<>(ResponseDto.SUCCESS, "주문이 생성되었습니다. 배송을 확인해주세요.", orderResponseDto);
    } else {
      return new ResponseDto<>(ResponseDto.FAILURE, orderResponseDto.getMessage());
    }
  }

  @GetMapping("/{orderId}")
  public ResponseDto<OrderResponseDto> getOrder(
      @PathVariable UUID orderId,
      @RequestHeader(value = "X-User-Name") String userName
  ) {

    OrderResponseDto orderResponseDto = orderService.getOrder(orderId, userName);

    return new ResponseDto<>(ResponseDto.SUCCESS, "주문이 조회되었습니다.", orderResponseDto);
  }

  @GetMapping
  public ResponseDto<PagedModel<?>> getOrdersPage(
      @RequestParam(required = false) List<UUID> uuidList,
      @QuerydslPredicate(root = Order.class) Predicate predicate,
      Pageable pageable,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
  ) {

    PagedModel<OrderResponseDto> orderResponseDtoPage
        = orderService.getOrdersPage(uuidList, predicate, pageable, userName, userRole);

    return new ResponseDto<>(ResponseDto.SUCCESS, "주문 목록이 조회되었습니다.", orderResponseDtoPage);
  }

  @PatchMapping("/{orderId}")
  public ResponseDto<OrderResponseDto> updateOrder(
      @PathVariable UUID orderId,
      @RequestBody OrderRequestDto orderRequestDto,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
  ) {

    OrderResponseDto orderResponseDto = orderService.updateOrder(orderId, orderRequestDto, userName,
        userRole);

    return new ResponseDto<>(ResponseDto.SUCCESS, "주문이 수정되었습니다.", orderResponseDto);
  }

  @DeleteMapping("/{orderId}")
  public ResponseDto<?> deleteOrder(
      @PathVariable UUID orderId,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String userRole
  ) {

    OrderResponseDto orderResponseDto = orderService.deleteOrder(orderId, userName, userRole);

    return new ResponseDto<>(ResponseDto.SUCCESS,
        "OrderId " + orderResponseDto.getOrderID() + " has been deleted.");
  }


}
