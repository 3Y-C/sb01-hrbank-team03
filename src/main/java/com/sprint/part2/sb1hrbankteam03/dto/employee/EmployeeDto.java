package com.sprint.part2.sb1hrbankteam03.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
  private Long id;
  private String name;
  private String email;
  private String employeeNumber;
  private String departmentId;
  private String departmentName;
  private String position;
  private String hireDate;
  private String status;
  private Long profileImageId;
}
