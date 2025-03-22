package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.entity.Department;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

  // 특정 부서에 속한 직원 수를 카운트
  @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :departmentId")
  Integer countEmployeesByDepartmentId(@Param("departmentId") Long departmentId);

//  //PostgreSql
//  @Query("SELECT d FROM Department d WHERE " +
//      "(:nameOrDescription IS NULL OR " +
//      "d.name ILIKE '%' || :nameOrDescription || '%' OR " +
//      "d.description ILIKE '%' || :nameOrDescription || '%') " +
//      "AND (:startId IS NULL OR d.id > :startId)")
//  Page<Department> searchDepartments(@Param("nameOrDescription") String nameOrDescription,
//      @Param("startId") Long startId,
//      Pageable pageable);

  boolean existsByName(String name);

  // 전체 부서 수를 Integer로 반환
  @Query("SELECT COUNT(d) FROM Department d")
  Integer countDepartments();


  @Query("SELECT COUNT(d) FROM Department d WHERE "
      + "(:nameOrDescription IS NULL OR d.name LIKE %:nameOrDescription% OR d.description LIKE %:nameOrDescription%)")
  Integer countDepartmentsByCondition(@Param("nameOrDescription") String nameOrDescription);


  // ASC 전용
  @Query("SELECT d FROM Department d WHERE " +
      "(:nameOrDescription IS NULL OR " +
      "d.name ILIKE CONCAT('%', :nameOrDescription, '%') OR " +
      "d.description ILIKE CONCAT('%', :nameOrDescription, '%')) AND " +
      "(:lastFieldValue IS NULL OR d.name > :lastFieldValue) " +
      "ORDER BY d.name ASC")
  Page<Department> searchDepartmentsByNameAsc(@Param("nameOrDescription") String nameOrDescription,
      @Param("lastFieldValue") String lastFieldValue,
      Pageable pageable);

  // DESC 전용
  @Query("SELECT d FROM Department d WHERE " +
      "(:nameOrDescription IS NULL OR " +
      "d.name ILIKE CONCAT('%', :nameOrDescription, '%') OR " +
      "d.description ILIKE CONCAT('%', :nameOrDescription, '%')) AND " +
      "(:lastFieldValue IS NULL OR d.name < :lastFieldValue) " +
      "ORDER BY d.name DESC")
  Page<Department> searchDepartmentsByNameDesc(@Param("nameOrDescription") String nameOrDescription,
      @Param("lastFieldValue") String lastFieldValue,
      Pageable pageable);


  @Query("SELECT d FROM Department d WHERE " +
      "(:nameOrDescription IS NULL OR " +
      "d.name LIKE CONCAT('%', :nameOrDescription, '%') OR " +
      "d.description LIKE CONCAT('%', :nameOrDescription, '%')) AND " +
      "((CAST(:lastFieldValue AS date) IS NULL) OR " +
      "((d.name = CAST(:name AS String) AND d.established_date > CAST(:lastFieldValue AS date)) OR " +
      "(d.name != CAST(:name AS String) AND d.established_date >= CAST(:lastFieldValue AS date)))) " +
      "ORDER BY d.established_date ASC")
  Page<Department> searchDepartmentsByDateAscNativeASC(
      @Param("nameOrDescription") String nameOrDescription,
      @Param("lastFieldValue") LocalDate lastFieldValue,
      @Param("name") String name,
      Pageable pageable);


  @Query("SELECT d FROM Department d WHERE " +
      "(:nameOrDescription IS NULL OR " +
      "d.name LIKE CONCAT('%', :nameOrDescription, '%') OR " +
      "d.description LIKE CONCAT('%', :nameOrDescription, '%')) AND " +
      "(CAST(:lastFieldValue AS date) IS NULL OR " +
      "((d.id != CAST(:lastFieldId AS Long) AND d.established_date < CAST(:lastFieldValue AS date)) OR " +
      "(d.id = CAST(:lastFieldId AS Long) AND d.established_date <= CAST(:lastFieldValue AS date)))) " +
      "ORDER BY d.established_date DESC")
  Page<Department> searchDepartmentsByDateAscNativeDesc(
      @Param("nameOrDescription") String nameOrDescription,
      @Param("lastFieldValue") LocalDate lastFieldValue,
      @Param("name") String name,
      Pageable pageable);


//맨 처음 요청
@Query("SELECT d FROM Department d WHERE " +
    "(:nameOrDescription IS NULL OR " +
    "d.name ILIKE CONCAT('%', :nameOrDescription, '%') OR " +
    "d.description ILIKE CONCAT('%', :nameOrDescription, '%')) " +
    "AND (:startId IS NULL OR d.id > :startId)")
  Page<Department> searchDepartments(@Param("nameOrDescription") String nameOrDescription,
      @Param("startId") Long startId,
      Pageable pageable);

}
