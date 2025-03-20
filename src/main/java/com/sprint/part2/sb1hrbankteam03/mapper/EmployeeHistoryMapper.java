package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.ChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class EmployeeHistoryMapper {

  public ChangeLogDto toDto(EmployeeHistory employeeHistory){
    return new ChangeLogDto(
        employeeHistory.getId(),
        employeeHistory.getChangeType(),
        employeeHistory.getEmployeeNumber(),
        employeeHistory.getMemo(),
        employeeHistory.getIpAddress(),
        employeeHistory.getAt()
    );
  }

  public CursorPageResponseChangeLogDto fromSlice(Slice<ChangeLogDto> slice, String nextCursor, Long totalElements) {
    // 마지막 요소의 ID를 가져옴
    Long nextIdAfter = slice.getContent().isEmpty() ? null : slice.getContent().get(slice.getContent().size() - 1).getId();

    // 커서가 없으면 다음 페이지가 없다고 표시
    if (slice.getContent().isEmpty()) {
      nextCursor = null;
      nextIdAfter = null;
    }

    return new CursorPageResponseChangeLogDto(
        slice.getContent(),           // 현재 페이지의 데이터 목록
        nextCursor,                   // 다음 페이지의 커서
        nextIdAfter,                  // 마지막 ID
        slice.getSize(),              // 페이지 크기
        totalElements,                // 전체 요소 수
        slice.hasNext()               // 다음 페이지 여부
    );
  }

}
