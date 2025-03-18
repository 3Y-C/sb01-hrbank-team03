package com.sprint.part2.sb1hrbankteam03.dto.employeeHistory;

import com.sprint.part2.sb1hrbankteam03.entity.EmployeeChangeDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiffDto { //직원 정보 수정 이력 변경 내용 (상세 조회용)
  private String propertyName;
  private String before;
  private String after;
}