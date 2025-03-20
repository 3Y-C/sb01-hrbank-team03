package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.DiffDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeSnapshotDto;
import com.sprint.part2.sb1hrbankteam03.entity.ChangeType;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface EmployeeHistoryService {

  void recordHistoryFromSnapshot(EmployeeSnapshotDto before, EmployeeSnapshotDto after, ChangeType type, String memo, String ipAddress);

  CursorPageResponseChangeLogDto getChangeLogs(
      String employeeNumber, String memo, String ipAddress, ChangeType changeType,
      Instant atFrom, Instant atTo, String cursor, Long idAfter, String sortField,
      String sortDirection, Pageable pageable);

  List<DiffDto> getChangeDetails(Long historyId);

  long getChangeLogCount(Instant fromDate, Instant toDate);
}
