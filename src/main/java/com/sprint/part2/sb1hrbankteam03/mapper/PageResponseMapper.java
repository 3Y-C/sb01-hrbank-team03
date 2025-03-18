package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  // Slice로 변환 (커서 기반 페이지네이션)
  public <T> PageResponse<T> fromSlice(Slice<T> slice, Object nextCursor) {
    return new PageResponse<>(
        slice.getContent(),
        nextCursor,
        slice.getSize(),
        slice.hasNext(),
        null  // totalElements는 커서 기반 페이지네이션에서는 필요하지 않음
    );
  }

  // Page로 변환 (전체 요소 수를 포함하는 페이지네이션)
  public <T> PageResponse<T> fromPage(Page<T> page, Object nextCursor) {
    return new PageResponse<>(
        page.getContent(),
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}