package com.logistics.platform.product_service.application.service;

import com.logistics.platform.product_service.application.dto.UserResDto;
import com.logistics.platform.product_service.application.service.auth.AuthService;
import com.logistics.platform.product_service.application.service.company.CompanyService;
import com.logistics.platform.product_service.application.service.hub.HunService;
import com.logistics.platform.product_service.domain.model.Product;
import com.logistics.platform.product_service.domain.model.Role;
import com.logistics.platform.product_service.domain.repository.ProductRepository;
import com.logistics.platform.product_service.presentation.global.exception.CustomApiException;
import com.logistics.platform.product_service.presentation.request.ProductRequestDto;
import com.logistics.platform.product_service.presentation.response.ProductResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final AuthService authService;
  private final CompanyService companyService;
  private final HunService hunService;

  public ProductResponseDto createProduct(ProductRequestDto productRequestDto, String userName,
      String userRole) {

    // 현재도 해당 권한이 유효한지
    UserResDto user = validateCurrentRole(userName, userRole);
    // 업체담당자라면 업체 서비스에서 업체담당자가 맞는지 확인
    validateCompanyManager(productRequestDto, userName, userRole, user);
    // 담당 허브 관리자라면 담당 허브가 어디인지
    validateHubManager(productRequestDto, userName, userRole, user);

    // 상품명 중복 가능

    Product product = Product.builder()
        .productName(productRequestDto.getProductName())
        .price(productRequestDto.getPrice())
        .count(productRequestDto.getCount())
        .createdBy(userName)
        .companyId(productRequestDto.getCompanyId())
        .hubId(productRequestDto.getHubId())
        .build();

    product = productRepository.save(product);

    return new ProductResponseDto(product);
  }

  @Transactional(readOnly = true)
  public ProductResponseDto getProduct(UUID productId, String userName, String userRole) {

    // 현재도 해당 권한이 유효한지
    UserResDto user = validateCurrentRole(userName, userRole);

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 productId 입니다."));

    if (product.getIsDeleted()) {
      throw new CustomApiException("이미 삭제된 상품입니다.");
    }

    // 담당 허브 관리자라면 담당 허브만 조회
    if (user.getRole().equals(Role.HUB_MANAGER)) {
      UUID hubIdByManagerId = hunService.getHubIdByManagerId(user.getId(), userName, userRole);
      if (!hubIdByManagerId.equals(product.getHubId())) {
        throw new CustomApiException("담당 허브의 Product만 조회 할 수 있습니다.");
      }
    }

    return new ProductResponseDto(product);
  }

  @Transactional(readOnly = true)
  public PagedModel<ProductResponseDto> getProductsPage(
      List<UUID> uuidList, Predicate predicate, Pageable pageable, String userName,
      String userRole) {

    // 현재도 해당 권한이 유효한지
    UserResDto user = validateCurrentRole(userName, userRole);

    // 허브매니저일 때
    if (user.getRole().equals(Role.HUB_MANAGER)) {
      UUID hubIdByManagerId = hunService.getHubIdByManagerId(user.getId(), userName, userRole);
      Page<ProductResponseDto> productResponseDtoPage
          = productRepository.findAllByHubManager(uuidList, predicate, pageable, hubIdByManagerId);
      return new PagedModel<>(productResponseDtoPage);
    }

    Page<ProductResponseDto> productResponseDtoPage
        = productRepository.findAll(uuidList, predicate, pageable);

    return new PagedModel<>(productResponseDtoPage);
  }

  @Transactional
  public ProductResponseDto updateProduct(UUID productId, ProductRequestDto productRequestDto,
      String userName, String userRole) {

    // 현재도 해당 권한이 유효한지
    UserResDto user = validateCurrentRole(userName, userRole);
    // 업체담당자라면 업체 서비스에서 업체담당자가 맞는지 확인
    validateCompanyManager(productRequestDto, userName, userRole, user);
    // 담당 허브 관리자라면 담당 허브가 어디인지
    validateHubManager(productRequestDto, userName, userRole, user);

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 productId입니다."));

    if (product.getIsDeleted()) {
      throw new CustomApiException("이미 삭제된 상품입니다.");
    }

    product.update(productRequestDto, userName);

    return new ProductResponseDto(product);
  }

  @Transactional
  public ProductResponseDto deleteProduct(UUID productId, String userName, String userRole) {

    // 현재도 해당 권한이 유효한지
    // 마스터, 담당 허브 관리자만 삭제 가능
    UserResDto user = validateCurrentRole(userName, userRole);
    if (userRole.equals(Role.COMPANY_MANAGER.getRole()) || userRole.equals(
        Role.DELIVERY_MANAGER.getRole())) {
      throw new CustomApiException("권한 부족");
    }

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 productId입니다."));

    // 허브담당자의 담당허브 Product가 아니라면
    if (user.getRole().equals(Role.HUB_MANAGER)) {
      UUID hubIdByManagerId = hunService.getHubIdByManagerId(user.getId(), userName, userRole);
      if (!hubIdByManagerId.equals(product.getHubId())) {
        throw new CustomApiException("담당 허브의 Product가 아닙니다.");
      }
    }

    if (product.getIsDeleted()) {
      throw new CustomApiException("이미 삭제된 상품입니다.");
    }

    product.delete(userName);

    return new ProductResponseDto(product);
  }

  @Transactional
  public void adjustProductQuantity(UUID productId, Long quantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 productId입니다."));

    if (product.getIsDeleted()) {
      throw new CustomApiException("이미 삭제된 상품입니다.");
    }

    productRepository.save(product.adjustCount(quantity));
  }

  public ProductResponseDto getProductWithOutValidateRole(UUID productId) {

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomApiException("존재하지 않는 productId 입니다."));

    if (product.getIsDeleted()) {
      throw new CustomApiException("이미 삭제된 상품입니다.");
    }

    return new ProductResponseDto(product);
  }


  private void validateHubManager(ProductRequestDto productRequestDto, String userName,
      String userRole,
      UserResDto user) {
    if (user.getRole().equals(Role.HUB_MANAGER)) {
      UUID hubIdByManagerId = hunService.getHubIdByManagerId(user.getId(), userName, userRole);
      if (!hubIdByManagerId.equals(productRequestDto.getHubId())) {
        throw new CustomApiException("담당 허브만 관리 할 수 있습니다.");
      }
    }
  }

  private void validateCompanyManager(ProductRequestDto productRequestDto, String userName,
      String userRole,
      UserResDto user) {
    Role currentRole = user.getRole();
    if (currentRole.equals(Role.COMPANY_MANAGER)) {
      Long companyManagerId = companyService.getCompanyInfo(
          productRequestDto.getCompanyId(), userName, userRole).getCompanyManagerId();
      Long userId = authService.getUserInfoById(
          user.getId(), user.getUsername(), user.getRole().getRole()).getId();
      if (!companyManagerId.equals(userId)) {
        throw new CustomApiException("본인 업체만 접근 할 수 있습니다.");
      }
    }
  }

  private UserResDto validateCurrentRole(String userName, String userRole) {
    UserResDto user = authService.getUserInfo(userName, userRole);
    Role currentRole = user.getRole();
    if (!userRole.equals(currentRole.getRole())) {
      throw new CustomApiException("User Role이 일치하지 않습니다. 다시 로그인해주세요.");
    }
    return user;
  }
}
