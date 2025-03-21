package com.sprint.part2.sb1hrbankteam03.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequest {
  private String name;
  private String email;
  private Long departmentId;
  private String position;
  private String  hireDate;
  private String status;
  private String memo;
}
