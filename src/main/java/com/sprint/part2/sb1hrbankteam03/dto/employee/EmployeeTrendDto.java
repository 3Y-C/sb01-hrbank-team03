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
  private long count;
  private long change;
  private double changeRate;

  public EmployeeTrendDto(String date, long count, long change, double changeRate) {
    this.date = date;
    this.count = count;
    this.change = change;
    this.changeRate = changeRate;
  }
}
