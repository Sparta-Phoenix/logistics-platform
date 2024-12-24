package com.logistics.platform.company_service.application.service;

import com.logistics.platform.company_service.application.dto.HubResponseDto;
import com.logistics.platform.company_service.application.dto.UserResDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final HubService hubService;
  private final UserService userService;

  public CompanyResponse createCompany(CompanyCreateRequest companyCreateRequest, String role,
      String userName) {
    if (role.equals("ROLE_MASTER")) {
    } else if (role.equals("ROLE_HUB_MANAGER")) {
      HubResponseDto hubDto = hubService.getHubDtoByHubId(companyCreateRequest.getHubId());
      UserResDto user = userService.getUserId(role, userName);
      if (!hubDto.getHubManagerId().equals(user.getId())) {
        throw new CustomApiException("권한이 없습니다.");
      }
    } else {
      throw new CustomApiException("권한이 없습니다.");
    }

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
        .roadAddress(companyCreateRequest.getRoadAddress())
        .companyType(companyTypeSet)
        .createdBy(userName)
        .isDeleted(false)
        .build();
    Company savedCompany = companyRepository.save(company);
    return new CompanyResponse(savedCompany);
  }

  @Transactional(readOnly = true)
  public CompanyResponse getCompany(UUID companyId, String role) {
    if (role.equals("ROLE_MASTER") || role.equals("ROLE_HUB_MANAGER") || role.equals(
        "ROLE_DELIVERY_MANAGER") || role.equals("ROLE_COMPANY_MANAGER")) {
    } else {
      throw new CustomApiException("권한이 없습니다.");
    }

    Company company = companyRepository.findByCompanyIdAndIsDeletedFalse(companyId).orElseThrow(
        () -> new CustomApiException("해당 companyId가 존재하지 않습니다."));

    return new CompanyResponse(company);
  }

  @Transactional(readOnly = true)
  public Page<CompanyResponse> searchCompanies(String keyword, Pageable pageable, String role) {
    if (role.equals("ROLE_MASTER") || role.equals("ROLE_HUB_MANAGER") || role.equals(
        "ROLE_DELIVERY_MANAGER") || role.equals("ROLE_COMPANY_MANAGER")) {
    } else {
      throw new CustomApiException("권한이 없습니다.");
    }

    int size = pageable.getPageSize();
    size = (size == 30 || size == 50) ? size : 10;

    Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by(
        Sort.Order.desc("createdAt"),
        Sort.Order.desc("updatedAt")
    );

    pageable = PageRequest.of(pageable.getPageNumber(), size, sort);

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
  public CompanyResponse modifyCompany(UUID companyId, CompanyModifyRequest companyModifyRequest,
      String role, String userName) {
    if (role.equals("ROLE_MASTER")) {
    } else if (role.equals("ROLE_HUB_MANAGER")) {
      HubResponseDto hubDto = hubService.getHubDtoByHubId(companyModifyRequest.getHubId());
      UserResDto user = userService.getUserId(role, userName);
      if (!hubDto.getHubManagerId().equals(user.getId())) {
        throw new CustomApiException("권한이 없습니다.");
      }
    } else if (role.equals("ROLE_COMPANY_MANAGER")) {
      Company company = companyRepository.findByCompanyIdAndIsDeletedFalse(companyId).orElseThrow(
          () -> new CustomApiException("해당 companyId가 존재하지 않습니다."));
      UserResDto user = userService.getUserId(role, userName);
      if (!company.getCompanyManagerId().equals(user.getId())) {
        throw new CustomApiException("권한이 없습니다.");
      }
    }

    Company company = companyRepository.findByCompanyIdAndIsDeletedFalse(companyId).orElseThrow(
        () -> new CustomApiException("해당 companyId가 존재하지 않습니다."));
    company.changeCompany(companyModifyRequest, userName);
    Company savedCompany = companyRepository.save(company);
    return new CompanyResponse(savedCompany);
  }

  @Transactional
  public void deleteCompany(UUID companyId, String role, String userName) {
    if (role.equals("ROLE_MASTER")) {
    } else if (role.equals("ROLE_HUB_MANAGER")) {
      Company company = companyRepository.findByCompanyIdAndIsDeletedFalse(companyId).orElseThrow(
          () -> new CustomApiException("해당 companyId가 존재하지 않습니다."));
      HubResponseDto hubDto = hubService.getHubDtoByHubId(company.getHubId());
      UserResDto user = userService.getUserId(role, userName);

      if (!hubDto.getHubManagerId().equals(user.getId())) {
        throw new CustomApiException("권한이 없습니다.");
      }
    } else {
      throw new CustomApiException("권한이 없습니다.");
    }
    Company company = companyRepository.findByCompanyIdAndIsDeletedFalse(companyId).orElseThrow(
        () -> new CustomApiException("해당 companyId가 존재하지 않습니다."));
    company.deleteCompany(userName);
  }

  public UUID getCompanyHubId(UUID companyId) {
    Company company = companyRepository.findByCompanyIdAndIsDeletedFalse(companyId).orElseThrow(
        () -> new CustomApiException("해당 companyId가 존재하지 않습니다."));
    return company.getHubId();
  }
}
