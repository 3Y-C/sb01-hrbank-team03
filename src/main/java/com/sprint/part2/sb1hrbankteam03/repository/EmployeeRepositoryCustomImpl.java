package com.sprint.part2.sb1hrbankteam03.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.part2.sb1hrbankteam03.entity.Employee;
import com.sprint.part2.sb1hrbankteam03.entity.QDepartment;
import com.sprint.part2.sb1hrbankteam03.entity.QEmployee;
import com.sprint.part2.sb1hrbankteam03.entity.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  private final JPAQueryFactory queryFactory;

  public EmployeeRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<Employee> findEmployeesWithFilters(String keyword, String department, String position,
      String employeeNumber, LocalDate startDate, LocalDate endDate, Long idAfter, Status status,
      Pageable pageable) {
    QEmployee e = QEmployee.employee;
    QDepartment d=QDepartment.department;

    BooleanBuilder whereClause = new BooleanBuilder();

    if (keyword != null && !keyword.isEmpty()) {
      whereClause.and(
          e.name.containsIgnoreCase(keyword)
              .or(e.email.containsIgnoreCase(keyword)));
    }
    if (department != null && !department.isEmpty()) {
      whereClause.and(d.name.containsIgnoreCase(department));
    }
    if (position != null && !position.isEmpty()) {
      whereClause.and(e.position.containsIgnoreCase(position));
    }
    if (employeeNumber != null && !employeeNumber.isEmpty()) {
      whereClause.and(e.employeeNumber.containsIgnoreCase(employeeNumber));
    }
    if (startDate != null) {
      whereClause.and(e.hireDate.goe(startDate));
    }
    if (endDate != null) {
      whereClause.and(e.hireDate.loe(endDate));
    }
    if (idAfter != null) {
      whereClause.and(e.id.gt(idAfter));
    }
    if (status != null) {
      whereClause.and(e.status.eq(status));
    }


    return queryFactory
        .selectFrom(e)
        .leftJoin(e.department,d).fetchJoin()
        .where(whereClause)
        .orderBy(getOrderSpecifiers(pageable,e))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  private static final Map<String, String> SORT_FIELD_MAP = Map.of(
      "name", "name",
      "employeeNumber", "employeeNumber",
      "hireDate", "hireDate"
  );

  private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, QEmployee e) {
    return pageable.getSort().stream()
        .map(order -> {
          String fieldName = SORT_FIELD_MAP.getOrDefault(order.getProperty(), order.getProperty());
          PathBuilder<Employee> path = new PathBuilder<>(Employee.class, "employee");

          return order.isAscending()
              ? new OrderSpecifier<>(com.querydsl.core.types.Order.ASC, path.get(fieldName, Comparable.class))
              : new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, path.get(fieldName, Comparable.class));
        })
        .toArray(OrderSpecifier[]::new);
  }
}
