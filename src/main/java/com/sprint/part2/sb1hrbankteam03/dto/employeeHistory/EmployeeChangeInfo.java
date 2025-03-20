package com.sprint.part2.sb1hrbankteam03.dto.employeeHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmployeeChangeInfo {
  private final String field;
  private final String oldValue;
  private final String newValue;
}