package com.sprint.part2.sb1hrbankteam03.repository.custom;

import com.sprint.part2.sb1hrbankteam03.entity.enums.ChangeType;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import java.time.Instant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface EmployeeHistoryRepositoryCustom {
  Slice<EmployeeHistory> findAllWithFilters(
      String employeeNumber, String memo, String ipAddress, ChangeType changeType,
      Instant atFrom, Instant atTo, String cursor, Long idAfter, Pageable pageable,
      String sortField, String sortDirection);


  public long countByFilters(
      String employeeNumber, String memo, String ipAddress, ChangeType changeType,
      Instant atFrom, Instant atTo);
}
