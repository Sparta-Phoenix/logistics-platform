package com.logistics.platform.deliverymanagerservice.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "delivery_order_index")
public class DeliveryOrderIndex {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(unique = true, nullable = false)
  private DeliveryType deliveryType;

  @Setter
  @Column(nullable = false)
  private Long currentOrderIndex;

  public DeliveryOrderIndex(DeliveryType deliveryType, Long currentOrderIndex) {
    this.deliveryType = deliveryType;
    this.currentOrderIndex = currentOrderIndex;
  }

}
