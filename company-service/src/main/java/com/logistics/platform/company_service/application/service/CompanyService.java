package com.logistics.platform.company_service.application.service;

import com.logistics.platform.company_service.domain.model.Company;
import com.logistics.platform.company_service.domain.model.CompanyType;
import com.logistics.platform.company_service.domain.repository.CompanyRepository;
import com.logistics.platform.company_service.presentation.global.ex.CustomApiException;
import com.logistics.platform.company_service.presentation.request.CompanyCreateRequest;
import com.logistics.platform.company_service.presentation.request.CompanyModifyRequest;
import com.logistics.platform.company_service.presentation.response.CompanyResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;

  public CompanyResponse createCompany(CompanyCreateRequest companyCreateRequest) {

    CompanyType companyTypeSet = !companyCreateRequest.isCheckCompanyType() ? CompanyType.MANUFACTURER : CompanyType.RECEIVER;
    Company company = Company.builder()
        .hubId(companyCreateRequest.getHubId())
        .companyName(companyCreateRequest.getCompanyName())
        .number(companyCreateRequest.getNumber())
        .address(companyCreateRequest.getAddress())
        .companyType(companyTypeSet)
        .isDeleted(false)
        .build();
    Company savedCompany = companyRepository.save(company);
    return new CompanyResponse(savedCompany);
  }

  public CompanyResponse getCompany(UUID companyId) {
    Company company = companyRepository.findByIdAble(companyId);
    if (company == null) {
      throw new CustomApiException("해당 companyId가 존재하지 않습니다.");
    }
    return new CompanyResponse(company);
  }

  public List<CompanyResponse> getAllCompany() {
    List<Company> companyList = companyRepository.findAllAble();
    List<CompanyResponse> companyResponseList = new ArrayList<>();
    for (Company company : companyList) {
      companyResponseList.add(new CompanyResponse(company));
    }
    return companyResponseList;
  }

  @Transactional
  public CompanyResponse modifyCompany(UUID companyId, CompanyModifyRequest companyModifyRequest) {
    Company company = companyRepository.findByIdAble(companyId);
    company.changeCompany(companyModifyRequest);
    Company savedCompany = companyRepository.save(company);
    return new CompanyResponse(savedCompany);
  }

  @Transactional
  public void deleteCompany(UUID companyId) {
    Company company = companyRepository.findByIdAble(companyId);
    company.deleteCompany();
  }
}
