package com.sprint.part2.sb1hrbankteam03.dto.backup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BackupDto {
  Integer id;
  String worker;
  String startedAt;
  String endedAt;
  String status;
  Integer fileId;
}
