package com.logistics.platform.company_service.domain.repository;

import com.logistics.platform.company_service.domain.model.Company;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

  Company findByCompanyIdAndIsDeletedFalse(UUID companyId);

  Company findByCompanyNameAndIsDeletedFalse(String companyName);

  Page<Company> findAllByIsDeletedFalse(Pageable pageable);

  Page<Company> findAllByCompanyNameContainingAndIsDeletedFalse(String keyword, Pageable pageable);

}