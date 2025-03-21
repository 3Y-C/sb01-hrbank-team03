package com.sprint.part2.sb1hrbankteam03.dto.employee;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursorPageResponseEmployeeDto {
  private List<EmployeeDto> content;
  private String nextCursor;
  private Long nextIdAfter;
  private int size;
  private int totalElements;
  private boolean hasNest;
}
