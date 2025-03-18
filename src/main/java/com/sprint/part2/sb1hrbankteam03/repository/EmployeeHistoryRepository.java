package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.entity.ChangeType;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {

  @Query("SELECT eh FROM EmployeeHistory eh " +
      "WHERE (:employeeNumber IS NULL OR eh.employeeNumber LIKE %:employeeNumber%) " +
      "AND (:memo IS NULL OR eh.memo LIKE %:memo%) " +
      "AND (:ipAddress IS NULL OR eh.ipAddress LIKE %:ipAddress%) " +
      "AND (:changeType IS NULL OR eh.changeType = :changeType) " +
      "AND (:atFrom IS NULL OR eh.at >= :atFrom) " +
      "AND (:atTo IS NULL OR eh.at <= :atTo) " +
      "ORDER BY " +
      "   CASE WHEN :sortField = 'ipAddress' THEN eh.ipAddress END, " +
      "   CASE WHEN :sortField = 'at' THEN eh.at END")
  Slice<EmployeeHistory> findAllWithFilters(
      @Param("employeeNumber") String employeeNumber,
      @Param("memo") String memo,
      @Param("ipAddress") String ipAddress,
      @Param("changeType") ChangeType changeType,
      @Param("atFrom") Instant atFrom,
      @Param("atTo") Instant atTo,
      @Param("cursor") Instant cursor,
      Pageable pageable
  );

  long countByAtBetween(LocalDateTime fromDate, LocalDateTime toDate);

  //변경 날짜가 instant 기준으로 이후인 모든 변경 이력을, 변경 날짜 기준으로 내림차순으로 가져옴
  List<EmployeeHistory> findByAtAfterOrderByAtDesc(Instant instant);
}
