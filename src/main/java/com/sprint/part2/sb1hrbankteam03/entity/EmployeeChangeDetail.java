package com.sprint.part2.sb1hrbankteam03.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.Getter;

@Getter
public class EmployeeChangeDetail extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "history_id")
  EmployeeHistory employeeHistory;

  String propertyName;
  String before;
  String after;
  Instant changedAt;
}
