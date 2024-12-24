package com.logistics.platform.slack_service.infrastructure.repository;

import com.logistics.platform.slack_service.presentation.response.SlackResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SlackRepositoryCustom {

  Page<SlackResponseDto> findAllToPage(List<UUID> uuidList, Predicate predicate, Pageable pageable);


}
