package com.sprint.part2.sb1hrbankteam03.dto.employeeHistory;

import com.sprint.part2.sb1hrbankteam03.entity.EmployeeChangeDetail;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmployeeChangeDetailDto {
  private String propertyName;
  private String beforeValue;
  private String afterValue;
  private Instant changedAt;

  public static EmployeeChangeDetailDto fromEntity(EmployeeChangeDetail entity) {
    return new EmployeeChangeDetailDto(entity.getPropertyName(), entity.getBefore(), entity.getAfter(), entity.getChangedAt());
  }
}