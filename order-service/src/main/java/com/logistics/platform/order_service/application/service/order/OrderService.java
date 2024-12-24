package com.logistics.platform.order_service.application.service.order;

import com.logistics.platform.order_service.application.dto.ProductResponseDto;
import com.logistics.platform.order_service.application.dto.Role;
import com.logistics.platform.order_service.application.dto.UserDto;
import com.logistics.platform.order_service.application.service.delivery.DeliveryService;
import com.logistics.platform.order_service.application.service.user.UserSerivce;
import com.logistics.platform.order_service.application.service.company.CompanyService;
import com.logistics.platform.order_service.application.service.product.ProductService;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductService productService;
  private final DeliveryService deliveryService;
  private final UserSerivce userSerivce;
  private final CompanyService companyService;

  @CircuitBreaker(name = "OrderService", fallbackMethod = "handleOrderFailure")
  public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, String userName,
      String userRole) {

    // ProductDto 형태로 응답을 받아 요청을 최소화
    ProductResponseDto product = productService.getProductDtoByProductId(
        orderRequestDto.getProductId(), userName, userRole);

    Long totalPrice = product.getPrice() * orderRequestDto.getProductQuantity();

    // 재고 확인
    if (product.getCount() < orderRequestDto.getProductQuantity()) {
      throw new CustomApiException("재고보다 주문 수량이 많습니다.");
    }

    // 주문 수량만큼 재고 차감
    productService.adjustProductQuantity(product.getProductId(),
        -orderRequestDto.getProductQuantity());

    Order order = Order.builder()
        .productId(orderRequestDto.getProductId())
        .supplyCompayId(orderRequestDto.getSupplyCompanyId())
        .receiveCompanyId(orderRequestDto.getReceiveCompanyId())
        .productQuantity(orderRequestDto.getProductQuantity())
        .totalPrice(totalPrice)
        .orderRequest(orderRequestDto.getOrderRequest())
        .createdBy(userName)
        .address(orderRequestDto.getAddress())
        .build();

    orderRepository.save(order);

    // 유저 서버의 유저 정보 호출
    UserDto userInfo = userSerivce.getUserInfo(userName, userRole);

    // 업체 서버의 업체 정보 호출
    UUID startHubId = companyService.getCompanyHubId(orderRequestDto.getSupplyCompanyId());
    UUID endHubId = companyService.getCompanyHubId(orderRequestDto.getReceiveCompanyId());

    // 배송 서버에 배송 생성 호출
    deliveryService.createDelivery(userInfo.getId(), startHubId, endHubId, order.getId(),
        userName, userInfo.getSlackId(), order.getAddress(), userName, userRole);

    return new OrderResponseDto(order);
  }

  public OrderResponseDto getOrder(UUID orderId, String userName) {

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomApiException("This Order ID does not exist."));

    if (order.getIsDeleted()) {
      throw new CustomApiException("This order has been deleted.");
    }

    if (!order.getCreatedBy().equals(userName)) {
      throw new CustomApiException("본인의 주문만 조회 할 수 있습니다.");
    }

    return new OrderResponseDto(order);
  }

  public PagedModel<OrderResponseDto> getOrdersPage(
      List<UUID> uuidList, Predicate predicate, Pageable pageable, String userName,
      String userRole) {

    Page<OrderResponseDto> orderResponseDtoPage = orderRepository.findAllToPage(uuidList, predicate,
        pageable, userName);

    return new PagedModel<>(orderResponseDtoPage);
  }

  @Transactional
  @CircuitBreaker(name = "OrderService", fallbackMethod = "handleOrderFailure")
  public OrderResponseDto updateOrder(UUID orderId, OrderRequestDto orderRequestDto,
      String userName, String userRole) {

    // 현재 권한이 유지되고 있는지 확인
    UserDto user = userSerivce.getUserInfo(userName, userRole);
    validateCurrentRole(userRole, user);

    // 마스터는 모두, 허브담당자는 담당 허브만 수정 가능
    if (!user.getRole().equals(Role.MASTER)) {
      if(!user.getRole().equals(Role.HUB_MANAGER)) {
        throw new CustomApiException("권한 없음");
      }
      // TODO 배송경로의 허브중 담당 허브가 있는지 확인
    }

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomApiException("This Order ID does not exist."));

    if (order.getIsDeleted()) {
      throw new CustomApiException("This order has been deleted.");
    }

    // TODO 이미 배송중이라면 수정 불가

    Long totalPrice = null;
    if (orderRequestDto.getProductQuantity() != null) {
      ProductResponseDto product = productService.getProductDtoByProductId(
          orderRequestDto.getProductId(), userName, userRole);
      totalPrice = product.getPrice() * orderRequestDto.getProductQuantity();
      if (product.getCount() < orderRequestDto.getProductQuantity() - order.getProductQuantity()) {
        throw new CustomApiException("추가 주문시 재고보다 주문 수량이 많습니다.");
      }

      // 이전 주문과 비교하여 재고 증감
      productService.adjustProductQuantity(product.getProductId(),
          order.getProductQuantity() - orderRequestDto.getProductQuantity());
    }

    order.update(
        orderRequestDto.getProductId(),
        orderRequestDto.getSupplyCompanyId(),
        orderRequestDto.getReceiveCompanyId(),
        orderRequestDto.getProductQuantity(),
        orderRequestDto.getOrderRequest(),
        totalPrice,
        userName,
        orderRequestDto.getAddress()
    );

    return new OrderResponseDto(order);
  }

  @Transactional
  public OrderResponseDto deleteOrder(UUID orderId, String userName, String userRole) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomApiException("This Order ID does not exist."));

    if (order.getIsDeleted()) {
      throw new CustomApiException("This order has already been deleted.");
    }

    // 현재 권한이 유지되고 있는지 확인
    UserDto user = userSerivce.getUserInfo(userName, userRole);
    validateCurrentRole(userRole, user);

    // 마스터는 모두, 허브담당자는 담당 허브만 수정 가능
    if (!user.getRole().equals(Role.MASTER)) {
      if(!user.getRole().equals(Role.HUB_MANAGER)) {
        throw new CustomApiException("권한 없음");
      }
      // TODO 배송경로의 허브중 담당 허브가 있는지 확인
    }

    // TODO 이미 배송중이라면 삭제 불가

    // 재고 수량 복구
    productService.adjustProductQuantity(order.getProductId(), -order.getProductQuantity());

    order.delete(userName);

    return new OrderResponseDto(order);
  }


  private void validateCurrentRole(String userRole, UserDto user) {
    if (!user.getRole().getRole().equals(userRole)) {
      throw new CustomApiException("권한이 일치하지 않습니다. 재로그인 해주세요.");
    }
  }


  public OrderResponseDto handleOrderFailure(OrderRequestDto orderRequestDto, String userName,
      String userRole, Throwable t) {
    return new OrderResponseDto(t.getMessage());
  }
}
