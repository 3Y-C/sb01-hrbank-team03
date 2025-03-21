package com.sprint.part2.sb1hrbankteam03.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateRequest {
  private String name;
  private String email;
  private Long departmentId;
  private String position;
  private String hireDate;
  private String memo;
}
