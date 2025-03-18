package com.sprint.part2.sb1hrbankteam03.repository;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeChangeDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeChangeDetailRepository extends JpaRepository<EmployeeChangeDetail, Long> {
  List<EmployeeChangeDetail> findByEmployeeHistoryId(Long historyId);

}
