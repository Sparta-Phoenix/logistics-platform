package com.logistics.platform.order_service.application.service;

import com.logistics.platform.order_service.domain.model.Order;
import com.logistics.platform.order_service.domain.repository.OrderRepository;
import com.logistics.platform.order_service.presentation.global.exception.CustomApiException;
import com.logistics.platform.order_service.presentation.request.OrderRequestDto;
import com.logistics.platform.order_service.presentation.response.OrderResponseDto;
import com.querydsl.core.types.Predicate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductServiceImpl productService;

  // TODO CircuitBreaker 위치 고민해보기
  @CircuitBreaker(name = "OrderService", fallbackMethod = "handlecreateOrderFailue")
  public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {

    // productId 검증
    if (!productService.validateProductId(orderRequestDto.getProductId())) {
      throw new CustomApiException("This Product ID does not exist.");
    }

    // product price 반환
    Long totalPrice =
        productService.getPriceByProductId(orderRequestDto.getProductId())
            * orderRequestDto.getProductQuantity();

    // supplyCompanyId 검증

    // receiveCompanyId 검증

    Order order = Order.builder()
        .productId(orderRequestDto.getProductId())
        .supplyCompayId(orderRequestDto.getSupplyCompanyId())
        .receiveCompanyId(orderRequestDto.getReceiveCompanyId())
        .productQuantity(orderRequestDto.getProductQuantity())
        .totalPrice(totalPrice)
        .orderRequest(orderRequestDto.getOrderRequest())
        .createdBy("createdBy") // TODO 생성자 추가
        .build();

    // TODO 배송 생성 추가

    orderRepository.save(order);

    return new OrderResponseDto(order);
  }

  public OrderResponseDto handlecreateOrderFailue(OrderRequestDto orderRequestDto, Throwable t) {
    return new OrderResponseDto(t.getMessage());
  }


  public OrderResponseDto getOrder(UUID orderId) {

    // TODO 본인 주문만 조회되도록 수정
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomApiException("This Order ID does not exist."));

    if (order.getIsDeleted()) {
      throw new CustomApiException("This Order was deleted.");
    }

    return new OrderResponseDto(order);
  }

  public PagedModel<OrderResponseDto> getOrdersPage(
      List<UUID> uuidList, Predicate predicate, Pageable pageable) {

    // TODO 본인 주문만 조회되도록 수정
    Page<OrderResponseDto> orderResponseDtoPage = orderRepository.findAllToPage(uuidList, predicate,
        pageable);

    return new PagedModel<>(orderResponseDtoPage);
  }
}
