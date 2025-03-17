package com.sprint.part2.sb1hrbankteam03.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeTrendDto {
  private String  date;
  private int count;
  private int change;
  private double changeRate;

  public EmployeeTrendDto(String date, int count, int change, double changeRate) {
    this.date = date;
    this.count = count;
    this.change = 0;
    this.changeRate = 0.0;
  }
}
