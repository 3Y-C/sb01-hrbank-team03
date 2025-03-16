package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

  @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :departmentId")
  long countEmployeesByDepartmentId(@Param("departmentId") Long departmentId);

  @Query("SELECT d FROM Department d WHERE " +
      "(:nameOrDescription IS NULL OR " +
      "LOWER(d.name) LIKE LOWER(CONCAT('%', :nameOrDescription, '%')) OR " +
      "LOWER(d.description) LIKE LOWER(CONCAT('%', :nameOrDescription, '%'))) " +
      "AND (:startId IS NULL OR d.id > :startId)")
  Page<Department> searchDepartments(@Param("nameOrDescription") String nameOrDescription,
      @Param("startId") Long startId,
      Pageable pageable);

  boolean existsByName(String name);

}
