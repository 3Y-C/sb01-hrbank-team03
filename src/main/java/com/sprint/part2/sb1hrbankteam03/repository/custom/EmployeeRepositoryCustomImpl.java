package com.sprint.part2.sb1hrbankteam03.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.part2.sb1hrbankteam03.entity.Employee;
import com.sprint.part2.sb1hrbankteam03.entity.QDepartment;
import com.sprint.part2.sb1hrbankteam03.entity.QEmployee;
import com.sprint.part2.sb1hrbankteam03.entity.enums.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  private final JPAQueryFactory queryFactory;

  public EmployeeRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  private static final Map<String, String> SORT_FIELD_MAP = Map.of(
      "name", "name",
      "employeeNumber", "employeeNumber",
      "hireDate", "hireDate"
  );

  @Override
  public Slice<Employee> findEmployeesWithFilters(
      String keyword, String department, String position,
      String employeeNumber, LocalDate startDate, LocalDate endDate,
      String cursor, Long idAfter, String sortField, String sortDirection,
      Status status, Pageable pageable
  ) {
    QEmployee e = QEmployee.employee;
    QDepartment d = QDepartment.department;

    BooleanBuilder whereClause = new BooleanBuilder();

    // 기본 검색 조건
    if (keyword != null && !keyword.isEmpty()) {
      whereClause.and(e.name.containsIgnoreCase(keyword)
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
    if (status != null) {
      whereClause.and(e.status.eq(status));
    }

    // 커서 조건 추가
    whereClause.and(buildCursorCondition(e, cursor, idAfter, sortField, sortDirection));

    // size + 1 조회로 다음 페이지 존재 여부 판단
    int pageSize = pageable.getPageSize() + 1;

    List<Employee> content = queryFactory
        .selectFrom(e)
        .leftJoin(e.department, d).fetchJoin()
        .where(whereClause)
        .orderBy(getOrderSpecifiers(pageable, e))
        .limit(pageSize)
        .fetch();

    boolean hasNext = content.size() > pageable.getPageSize();
    if (hasNext) {
      content.remove(content.size() - 1); // 초과 데이터 제거
    }

    return new SliceImpl<>(content, pageable, hasNext);
  }

  // 커서 조건 빌더
  private BooleanBuilder buildCursorCondition(QEmployee e, String cursor, Long idAfter, String sortField, String sortDirection) {
    BooleanBuilder builder = new BooleanBuilder();
    if (cursor == null || cursor.isEmpty()) return builder;

    boolean isDesc = "desc".equalsIgnoreCase(sortDirection);

    switch (sortField) {
      case "name":
        if (isDesc) {
          builder.and(e.name.lt(cursor).or(e.name.eq(cursor).and(e.id.lt(idAfter))));
        } else {
          builder.and(e.name.gt(cursor).or(e.name.eq(cursor).and(e.id.gt(idAfter))));
        }
        break;
      case "hireDate":
        LocalDate hireCursor = LocalDate.parse(cursor);
        if (isDesc) {
          builder.and(e.hireDate.lt(hireCursor).or(e.hireDate.eq(hireCursor).and(e.id.lt(idAfter))));
        } else {
          builder.and(e.hireDate.gt(hireCursor).or(e.hireDate.eq(hireCursor).and(e.id.gt(idAfter))));
        }

        break;
      case "employeeNumber":
        builder.and(isDesc
            ? e.employeeNumber.lt(cursor).or(e.employeeNumber.eq(cursor).and(e.id.lt(idAfter)))
            : e.employeeNumber.gt(cursor).or(e.employeeNumber.eq(cursor).and(e.id.gt(idAfter))));
        break;
      default:
        if (idAfter != null) {
          builder.and(isDesc ? e.id.lt(idAfter) : e.id.gt(idAfter));
        }
        break;
    }

    return builder;
  }

  // 정렬 조건
  // 정렬 조건
  private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, QEmployee e) {
    List<OrderSpecifier<?>> specifiers = pageable.getSort().stream()
        .map(order -> {
          String fieldName = SORT_FIELD_MAP.getOrDefault(order.getProperty(), order.getProperty());
          PathBuilder<Employee> path = new PathBuilder<>(Employee.class, "employee");

          return order.isAscending()
              ? new OrderSpecifier<>(Order.ASC, path.get(fieldName, Comparable.class))
              : new OrderSpecifier<>(Order.DESC, path.get(fieldName, Comparable.class));
        }).collect(Collectors.toList());

    // 항상 id 정렬 추가 (tie-breaker)
    boolean isIdSorted  = pageable.getSort().stream()
        .anyMatch(order -> "id".equals(order.getProperty()));
    if (!isIdSorted) {
      boolean isDesc = pageable.getSort().stream()
          .findFirst()
          .map(order -> !order.isAscending())
          .orElse(false);

      specifiers.add(isDesc ? new OrderSpecifier<>(Order.DESC, e.id) : new OrderSpecifier<>(Order.ASC, e.id));
    }
    return specifiers.toArray(new OrderSpecifier[0]);
  }

}
