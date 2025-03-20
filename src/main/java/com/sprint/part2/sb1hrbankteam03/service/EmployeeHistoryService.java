package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.DiffDto;
import com.sprint.part2.sb1hrbankteam03.entity.ChangeType;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeChangeDetail;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface EmployeeHistoryService {

  EmployeeHistory createHistory(String employeeNumber, ChangeType type, String memo);

  CursorPageResponseChangeLogDto getChangeLogs(
      String employeeNumber, String memo, String ipAddress, ChangeType changeType,
      Instant atFrom, Instant atTo, String cursor, Long idAfter, String sortField,
      String sortDirection, Pageable pageable);

  void saveChangeDetails(EmployeeHistory history, List<EmployeeChangeDetail> details);

  List<DiffDto> getChangeDetails(Long historyId);

  long getChangeLogCount(Instant fromDate, Instant toDate);
}
