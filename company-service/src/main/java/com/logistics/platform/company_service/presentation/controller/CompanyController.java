package com.logistics.platform.company_service.presentation.controller;

import com.logistics.platform.company_service.application.service.CompanyService;
import com.logistics.platform.company_service.presentation.global.ResponseDto;
import com.logistics.platform.company_service.presentation.request.CompanyCreateRequest;
import com.logistics.platform.company_service.presentation.request.CompanyModifyRequest;
import com.logistics.platform.company_service.presentation.response.CompanyResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @PostMapping
  public ResponseDto<CompanyResponse> create(@RequestBody @Valid CompanyCreateRequest companyCreateRequest) {

    CompanyResponse companyResponse = companyService.createCompany(companyCreateRequest);
    return new ResponseDto<>(1, "업체가 생성되었습니다.", companyResponse);
  }

  @GetMapping("/{companyId}")
  public ResponseDto<CompanyResponse> get(@PathVariable UUID companyId) {
    CompanyResponse companyResponse = companyService.getCompany(companyId);
    return new ResponseDto<>(1, "업체 단건 조회가 완료되었습니다.", companyResponse);
  }

  @GetMapping
  public ResponseDto<List<CompanyResponse>> getAll() {
    List<CompanyResponse> companyResponseList = companyService.getAllCompany();
    return new ResponseDto<>(1, "업체 목록 조회가 완료되었습니다.", companyResponseList);
  }

  @PatchMapping("/{companyId}")
  public ResponseDto<CompanyResponse> modify(@PathVariable UUID companyId,
      @RequestBody @Valid CompanyModifyRequest companyModifyRequest) {
    CompanyResponse companyResponse = companyService.modifyCompany(companyId, companyModifyRequest);
    return new ResponseDto<>(1, "업체 수정이 완료되었습니다.", companyResponse);
  }

  @PutMapping("/{companyId}")
  public ResponseDto<CompanyResponse> delete(@PathVariable UUID companyId) {
    companyService.deleteCompany(companyId);
    return new ResponseDto<>(1, "업체 삭제가 완료되었습니다.", null);
  }



}
