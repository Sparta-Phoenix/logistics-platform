package com.logistics.platform.delivery_service.delivery.domain.model;

public enum DeliveryStatus {

  WAITING_AT_HUB,        // 허브대기중
  MOVING_TO_HUB,         // 허브이동중
  ARRIVED_AT_DESTINATION_HUB,  // 목적지허브도착
  OUT_FOR_DELIVERY,      // 배송중
  DELIVERY_COMPLETED,  // 배송완료
  DELIVERY_CANCELLED,   // 배송취소
  DELIVERY_DELETED      // 배송삭제

}
