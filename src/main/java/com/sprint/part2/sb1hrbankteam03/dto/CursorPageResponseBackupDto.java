package com.sprint.part2.sb1hrbankteam03.dto;

import com.sprint.part2.sb1hrbankteam03.dto.backup.BackupDto;
import java.util.List;
import lombok.Getter;

@Getter
public class CursorPageResponseBackupDto {
  private List<BackupDto> contents;
  private String nextCursor;
  private Long nextIdAfter;
  private Integer size;
  private Long totalElements;
  private boolean hasNext;

}
