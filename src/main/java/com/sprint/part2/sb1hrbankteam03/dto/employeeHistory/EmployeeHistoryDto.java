package com.sprint.part2.sb1hrbankteam03.dto.employeeHistory;

import com.sprint.part2.sb1hrbankteam03.entity.ChangeType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmployeeHistoryDto {
  private Long id;
  private String employeeNumber;
  private ChangeType changeType;
  private String description;
  private String ipAddress;
  private Instant editedAt;
}