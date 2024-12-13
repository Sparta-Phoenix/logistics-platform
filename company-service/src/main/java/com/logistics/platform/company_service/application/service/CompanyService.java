package com.logistics.platform.company_service.application.service;

import com.logistics.platform.company_service.application.dto.HubResponseDto;
import com.logistics.platform.company_service.domain.model.Company;
import com.logistics.platform.company_service.domain.model.CompanyType;
import com.logistics.platform.company_service.domain.repository.CompanyRepository;
import com.logistics.platform.company_service.presentation.global.ex.CustomApiException;
import com.logistics.platform.company_service.presentation.request.CompanyCreateRequest;
import com.logistics.platform.company_service.presentation.request.CompanyModifyRequest;
import com.logistics.platform.company_service.presentation.response.CompanyResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final HubService hubService;

  public CompanyResponse createCompany(CompanyCreateRequest companyCreateRequest) {

    // todo 허브 존재 여부 검증
    HubResponseDto hubDto = hubService.getHubDtoByHubId(companyCreateRequest.getHubId());

    if (companyRepository.findByCompanyNameAndIsDeletedFalse(
        companyCreateRequest.getCompanyName()).isPresent()) {
      throw new CustomApiException("해당 업체 이름이 이미 존재합니다.");
    }

    CompanyType companyTypeSet =
        !companyCreateRequest.getIsCompanyTypeReceiver() ? CompanyType.MANUFACTURER
            : CompanyType.RECEIVER;
    Company company = Company.builder()
        .hubId(companyCreateRequest.getHubId())
        .companyManagerId(companyCreateRequest.getCompanyManagerId())
        .companyName(companyCreateRequest.getCompanyName())
        .phoneNumber(companyCreateRequest.getPhoneNumber())
        .address(companyCreateRequest.getAddress())
        .companyType(companyTypeSet)
        .createdBy("임시 생성자")
        .isDeleted(false)
        .build();
    Company savedCompany = companyRepository.save(company);
    return new CompanyResponse(savedCompany);
  }

  @Transactional(readOnly = true)
  public CompanyResponse getCompany(UUID companyId) {
    Company company = companyRepository.findByCompanyIdAndIsDeletedFalse(companyId).orElseThrow(
        () -> new CustomApiException("해당 companyId가 존재하지 않습니다."));

    return new CompanyResponse(company);
  }

  @Transactional(readOnly = true)
  public Page<CompanyResponse> searchCompanies(String keyword, Pageable pageable) {
    Page<Company> companies;
    if (keyword == null || keyword.trim().isEmpty()) {
      companies = companyRepository.findAllByIsDeletedFalse(pageable);
    } else {
      companies = companyRepository.findAllByCompanyNameContainsIgnoreCaseAndIsDeletedFalse(
          keyword, pageable);
    }
    return companies.map(CompanyResponse::new);
  }

  @Transactional
  public CompanyResponse modifyCompany(UUID companyId, CompanyModifyRequest companyModifyRequest) {
    Company company = companyRepository.findByCompanyIdAndIsDeletedFalse(companyId).orElseThrow(
        () -> new CustomApiException("해당 companyId가 존재하지 않습니다."));
    company.changeCompany(companyModifyRequest);
    Company savedCompany = companyRepository.save(company);
    return new CompanyResponse(savedCompany);
  }

  @Transactional
  public void deleteCompany(UUID companyId) {
    Company company = companyRepository.findByCompanyIdAndIsDeletedFalse(companyId).orElseThrow(
        () -> new CustomApiException("해당 companyId가 존재하지 않습니다."));
    company.deleteCompany();
  }

}
