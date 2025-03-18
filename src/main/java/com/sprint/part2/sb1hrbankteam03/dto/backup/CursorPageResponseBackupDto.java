package com.sprint.part2.sb1hrbankteam03.dto.backup;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CursorPageResponseBackupDto {

  private List<BackupDto> content;
  private String nextCursor;
  private Long nextIdAfter;
  private int size;
  private Long totalElements;
  private boolean hasNest;
}
