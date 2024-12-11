package com.logistics.platform.company_service.domain.repository;

import com.logistics.platform.company_service.domain.model.Company;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID>{
  @Query("select c from Company c where c.companyId = :companyId and c.isDeleted = false")
  Company findByIdAble(UUID companyId);

  @Query("select c from Company c where c.isDeleted = false")
  List<Company> findAllAble();

}
