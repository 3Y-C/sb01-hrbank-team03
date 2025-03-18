package com.sprint.part2.sb1hrbankteam03.dto.backup;

import java.util.List;

public class CursorPageResponseBackupDto {

  private List<BackupDto> content;
  private String nextCursor;
  private int nextIdAfter;
  private int size;
  private int totalElements;
  private boolean hasNest;
}
