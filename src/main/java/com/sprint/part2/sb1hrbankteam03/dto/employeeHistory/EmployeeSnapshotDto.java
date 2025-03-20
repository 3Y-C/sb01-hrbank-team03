package com.sprint.part2.sb1hrbankteam03.dto.employeeHistory;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmployeeSnapshotDto {
  private String employeeNumber;
  private String name;
  private String email;
  private String department;
  private String position;
  private String hireDate;
  private String status;
}
