package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.backup.BackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.backup.CursorPageResponseBackupDto;
import com.sprint.part2.sb1hrbankteam03.entity.Backup;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BackupMapper {

  public BackupDto toDto(Backup backup) {
    return new BackupDto(
        Integer.parseInt(String.valueOf(backup.getId())),
        backup.getWorkerIp(),
        backup.getStartAt().toString(),
        backup.getEndAt().toString(),
        backup.getStatus().toString(),
        Integer.parseInt(String.valueOf(backup.getId()))
    );
  }

  public CursorPageResponseBackupDto toPageDto(List<BackupDto> backups, Long nextCursor,
      Long nextIdAfter, int size, Long totalElements, boolean hasNext) {
    return new CursorPageResponseBackupDto(
        backups,
        nextCursor,
        nextIdAfter,
        size,
        totalElements,
        hasNext);
  }
}
