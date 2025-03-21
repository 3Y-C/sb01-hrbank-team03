package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.entity.Employee;
import com.sprint.part2.sb1hrbankteam03.entity.Status;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeRepositoryCustom {
  List<Employee> findEmployeesWithFilters(
      String keyword, String department, String position,
      String employeeNumber, LocalDate startDate, LocalDate endDate,
      Long idAfter, Status status, Pageable pageable
  );
}
