package com.logistics.platform.hub_service.domain.model;

import com.logistics.platform.hub_service.presentation.request.HubModifyRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "p_hub")
public class Hub {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID hubId;

  @Column(nullable = false)
  private Long hubManagerId;

  @Column(nullable = false)
  private String hubName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private HubType hubType;

  @Column(nullable = false)
  private String centerPostalCode;

  @Column(nullable = false)
  private String roadAddress;

  @Column(nullable = false)
  private String postalCode;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
  private Point location;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(updatable = false, nullable = false, length = 100)
  private String createdBy;

  @LastModifiedDate
  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(length = 100)
  private String updatedBy;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime deletedAt;

  @Column
  private String deletedBy;

  @Column(nullable = false)
  private Boolean isDeleted = false;

  public void changeHub(HubModifyRequest hubModifyRequest, String userName) {
    this.hubManagerId = hubModifyRequest.getHubManagerId();
    this.hubName = hubModifyRequest.getHubName();
    this.hubType = !hubModifyRequest.getIsHubTypeReceiver() ? HubType.localHub
        : HubType.centralHub;
    this.centerPostalCode = hubModifyRequest.getCenterPostalCode();
    this.roadAddress = hubModifyRequest.getRoadAddress();
    this.postalCode = hubModifyRequest.getPostalCode();
    this.deletedBy = userName;
  }

  public void deleteHub(String userName) {
    this.isDeleted = true;
    this.deletedBy = userName;
  }
}
