package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeHistoryDto;
import com.sprint.part2.sb1hrbankteam03.entity.ChangeType;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {
  @Query("SELECT new com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeHistoryDto(eh.id, eh.employeeNumber, eh.changeType, eh.description, eh.ipAddress, eh.editedAt) " +
      "FROM EmployeeHistory eh " +
      "WHERE (:employeeNumber IS NULL OR eh.employeeNumber LIKE %:employeeNumber%) " +
      "AND (:memo IS NULL OR eh.description LIKE %:memo%) " +
      "AND (:ipAddress IS NULL OR eh.ipAddress LIKE %:ipAddress%) " +
      "AND (:changeType IS NULL OR eh.changeType = :changeType) " +
      "AND (:startTime IS NULL OR eh.editedAt >= :startTime) " +
      "AND (:endTime IS NULL OR eh.editedAt <= :endTime)")
  Page<EmployeeHistoryDto> findChangeLogs(@Param("employeeNumber") String employeeNumber,
      @Param("memo") String memo,
      @Param("ipAddress") String ipAddress,
      @Param("changeType") ChangeType changeType,
      @Param("startTime") LocalDateTime startTime,
      @Param("endTime") LocalDateTime endTime,
      Pageable pageable);

  long countByEditedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
