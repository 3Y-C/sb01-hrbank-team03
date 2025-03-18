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

  // 특정 부서에 속한 직원 수를 카운트
  @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :departmentId")
  Integer countEmployeesByDepartmentId(@Param("departmentId") Long departmentId);

//  //부서 이름이나 설명을 기준으로 검색하고, 시작 ID 이상인 부서들을 조회
//  @Query("SELECT d FROM Department d WHERE " +
//      "(:nameOrDescription IS NULL OR " +
//      "LOWER(d.name) LIKE LOWER(CONCAT('%', :nameOrDescription, '%')) OR " +
//      "LOWER(d.description) LIKE LOWER(CONCAT('%', :nameOrDescription, '%'))) " +
//      "AND (:startId IS NULL OR d.id > :startId)")
//  Page<Department> searchDepartments(@Param("nameOrDescription") String nameOrDescription,
//      @Param("startId") Long startId,
//      Pageable pageable);


  //PostgreSql
  @Query("SELECT d FROM Department d WHERE " +
      "(:nameOrDescription IS NULL OR " +
      "d.name ILIKE '%' || :nameOrDescription || '%' OR " +
      "d.description ILIKE '%' || :nameOrDescription || '%') " +
      "AND (:startId IS NULL OR d.id > :startId)")
  Page<Department> searchDepartments(@Param("nameOrDescription") String nameOrDescription,
      @Param("startId") Long startId,
      Pageable pageable);


  boolean existsByName(String name);

  // 전체 부서 수를 Integer로 반환
  @Query("SELECT COUNT(d) FROM Department d")
  Integer countDepartments();

}
