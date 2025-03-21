package com.sprint.part2.sb1hrbankteam03.dto.employeeHistory;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CursorPageResponseChangeLogDto {
  private List<ChangeLogDto> content;
  private String nextCursor; //다음 페이지 커서
  private Long nextIdAfter; //마지막 요소의 ID
  private int size; //페이지 크기
  private Long totalElements; //총 요소수
  private boolean hasNext; //다음 페이지 여부
}
