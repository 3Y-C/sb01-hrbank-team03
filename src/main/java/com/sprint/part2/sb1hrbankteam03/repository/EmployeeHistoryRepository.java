package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long>
    , EmployeeHistoryRepositoryCustom {

  long countByAtBetween(Instant fromDate, Instant toDate);

  //변경 날짜가 instant 기준으로 이후인 모든 변경 이력을, 변경 날짜 기준으로 내림차순으로 가져옴
  List<EmployeeHistory> findByAtAfterOrderByAtDesc(Instant instant);
}
