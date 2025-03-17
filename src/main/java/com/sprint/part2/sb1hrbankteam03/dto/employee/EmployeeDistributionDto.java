package com.sprint.part2.sb1hrbankteam03.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDistributionDto {
  private String  groupKey;
  private int count;
  private double percentage;

  public EmployeeDistributionDto(String groupKey, int count, int percentage) {
    this.groupKey = groupKey;
    this.count = count;
    this.percentage = 0.0;
  }
}
