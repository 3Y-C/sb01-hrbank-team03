package com.sprint.part2.sb1hrbankteam03.dto.backup;

import com.sprint.part2.sb1hrbankteam03.entity.BackupStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
@AllArgsConstructor
public class ParsedBackupDto {

  private String workerIp;
  private BackupStatus backupStatus;
  private LocalDateTime startedAtFrom;
  private LocalDateTime startedAtTo;

  private int pageSize;
  private Long cursorId;
  private LocalDateTime cursorStartAt;
  private PageRequest pageRequest;
  private long totalElements;
}
