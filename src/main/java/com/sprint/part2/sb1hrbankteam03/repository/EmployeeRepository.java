package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDistributionDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeTrendDto;
import com.sprint.part2.sb1hrbankteam03.entity.Status;
import com.sprint.part2.sb1hrbankteam03.entity.Employee;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  boolean existsByEmail(String email);

  @Query(value = "SELECT DATE_FORMAT(e.hire_date, :format) AS date, COUNT(e.id) AS count " +
      "FROM employee e " +
      "WHERE e.hire_date BETWEEN :startDate AND :endDate " +
      "GROUP BY DATE_FORMAT(e.hire_date, :format) " +
      "ORDER BY DATE_FORMAT(e.hire_date, :format)",
      nativeQuery = true)
  List<EmployeeTrendDto> getEmployeeTrend(@Param("startDate") LocalDate start,
      @Param("endDate") LocalDate end, @Param("format") String timeUnit);

  @Query(value = "SELECT " +
      "   CASE WHEN :criteria = 'department' THEN d.name ELSE e.position END AS groupKey, " +
      "   COUNT(*) AS count " +
      "FROM employee e " +
      "LEFT JOIN department d ON e.department_id = d.id " +
      "WHERE e.status = :status " +
      "GROUP BY groupKey", nativeQuery = true)
  List<EmployeeDistributionDto> getEmployeeDistribution(@Param("criteria") String criteria,
      @Param("status") Status employeeStatus);


  //전체 직원 조회
  long countByStatus(Status employeeStatus);

  //특정 상태 및 기간 내 입사 직원
  long countByStatusAndHireDateBetween(Status status, LocalDate hireDateAfter,
      LocalDate hireDateBefore);

  //특정 기간내 입사(상태x)
  long countByStatusAndHireDateAfter(Status status, LocalDate hireDateAfter);

  @Query("SELECT e FROM Employee e " +
      "LEFT JOIN e.department d " +
      "WHERE (:keyword IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "                      OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
      "AND (:department IS NULL OR d IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :department, '%'))) " +
      "AND (:position IS NULL OR LOWER(e.position) LIKE LOWER(CONCAT('%', :position, '%'))) " +
      "AND (:employeeNumber IS NULL OR e.employeeNumber LIKE CONCAT('%', :employeeNumber, '%')) " +
      "AND (:status IS NULL OR e.status = :status) " +
      "AND (COALESCE(:startDate, e.hireDate) <= e.hireDate " +
      "     AND COALESCE(:endDate, e.hireDate) >= e.hireDate)")

  List<Employee> findEmployees(
      @Param("keyword") String keyword,
      @Param("department") String department,
      @Param("position") String position,
      @Param("employeeNumber") String employeeNumber,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("status") String status);

}
