package com.logistics.platform.deliverymanagerservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingFields {

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Comment("생성일")
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(nullable = false, updatable = false)
  @Comment("생성자")
  private String createdBy;

  @LastModifiedDate
  @Column
  @Temporal(TemporalType.TIMESTAMP)
  @Comment("수정일")
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column
  @Comment("수정자")
  private String updatedBy;


  @Column
  @Temporal(TemporalType.TIMESTAMP)
  @Comment("삭제일")
  private LocalDateTime deletedAt;

  @Column
  @Comment("삭제자")
  private String deletedBy;

  public void delete(String deletedBy) {
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = deletedBy;
  }

}
