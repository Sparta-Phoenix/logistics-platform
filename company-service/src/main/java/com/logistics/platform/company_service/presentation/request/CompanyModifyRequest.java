package com.logistics.platform.company_service.presentation.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CompanyModifyRequest {

  @NotNull
  private UUID hubId;

  @NotEmpty
  private String companyName;

  @Pattern(regexp = "^(01[016789]|02|031|032|033|041|042|043|044|051|052|053|054|055|061|062|063|064)-\\d{3,4}-\\d{4}$"
      , message = "전화번호는 010(개인 또는 지역번호)-1234-1234 형식으로 작성해주세요.")
  private String number;

  @NotEmpty
  private String address;

  private boolean checkCompanyType; // false = 생산 업체, true = 수령 업체

}
