package com.sprint.part2.sb1hrbankteam03.dto.employeeHistory;

import com.sprint.part2.sb1hrbankteam03.entity.enums.ChangeType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeLogDto { //직원 정보 수정 이력 (목록 조회용)
  private Long id;
  private ChangeType type;
  private String employeeNumber;
  private String memo;
  private String ipAddress;
  private Instant at;
}