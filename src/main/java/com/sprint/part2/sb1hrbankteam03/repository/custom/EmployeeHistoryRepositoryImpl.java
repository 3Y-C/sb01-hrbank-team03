package com.sprint.part2.sb1hrbankteam03.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.part2.sb1hrbankteam03.entity.enums.ChangeType;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import com.sprint.part2.sb1hrbankteam03.entity.QEmployeeHistory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeHistoryRepositoryImpl implements EmployeeHistoryRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public long countByFilters(
      String employeeNumber, String memo, String ipAddress, ChangeType changeType,
      Instant atFrom, Instant atTo) {

    QEmployeeHistory q = QEmployeeHistory.employeeHistory;
    JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(employeeNumberCondition(employeeNumber, q));
    builder.and(memoCondition(memo, q));
    builder.and(ipAddressCondition(ipAddress, q));
    builder.and(changeTypeCondition(changeType, q));
    builder.and(atFromCondition(atFrom, q));
    builder.and(atToCondition(atTo, q));

    return queryFactory.select(q.count())
        .from(q)
        .where(builder)
        .fetchOne();
  }

  @Override
  public Slice<EmployeeHistory> findAllWithFilters(
      String employeeNumber, String memo, String ipAddress, ChangeType changeType,
      Instant atFrom, Instant atTo, String cursor, Long idAfter, Pageable pageable,
      String sortField, String sortDirection) {

    QEmployeeHistory q = QEmployeeHistory.employeeHistory;
    JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(employeeNumberCondition(employeeNumber, q));
    builder.and(memoCondition(memo, q));
    builder.and(ipAddressCondition(ipAddress, q));
    builder.and(changeTypeCondition(changeType, q));
    builder.and(atFromCondition(atFrom, q));
    builder.and(atToCondition(atTo, q));

    BooleanExpression cursorCondition = buildCursorCondition(q, cursor, idAfter, sortField, sortDirection);
    if (cursorCondition != null) {
      builder.and(cursorCondition);
    }

    List<EmployeeHistory> results = queryFactory
        .selectFrom(q)
        .where(builder)
        .orderBy(getOrderSpecifiers(q, sortField, sortDirection))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1) // 다음 페이지 여부 확인용 +1
        .fetch();

    boolean hasNext = results.size() > pageable.getPageSize();

    if (hasNext) {
      results.remove(results.size() - 1); // 초과된 한 건 제거
    }

    return new SliceImpl<>(results, pageable, hasNext);
  }

  private BooleanExpression buildCursorCondition(QEmployeeHistory q, String cursor, Long idAfter, String sortField, String sortDirection) {
    if (cursor == null || idAfter == null) return null;

    boolean isDesc = "DESC".equalsIgnoreCase(sortDirection);

    if ("ipAddress".equals(sortField)) {
      return isDesc
          ? q.ipAddress.lt(cursor).or(q.ipAddress.eq(cursor).and(q.id.lt(idAfter)))
          : q.ipAddress.gt(cursor).or(q.ipAddress.eq(cursor).and(q.id.gt(idAfter)));
    } else if ("at".equals(sortField)) {
      Instant cursorAt = Instant.parse(cursor);
      return isDesc
          ? q.at.lt(cursorAt).or(q.at.eq(cursorAt).and(q.id.lt(idAfter)))
          : q.at.gt(cursorAt).or(q.at.eq(cursorAt).and(q.id.gt(idAfter)));
    }

    return null;
  }

  private OrderSpecifier<?>[] getOrderSpecifiers(QEmployeeHistory q, String sortField, String sortDirection) {
    boolean isDesc = "DESC".equalsIgnoreCase(sortDirection);

    if ("ipAddress".equals(sortField)) {
      return isDesc
          ? new OrderSpecifier[]{q.ipAddress.desc(), q.id.desc()}
          : new OrderSpecifier[]{q.ipAddress.asc(), q.id.asc()};
    } else if ("at".equals(sortField)) {
      return isDesc
          ? new OrderSpecifier[]{q.at.desc(), q.id.desc()}
          : new OrderSpecifier[]{q.at.asc(), q.id.asc()};
    }

    // 기본 정렬
    return new OrderSpecifier[]{q.at.desc(), q.id.desc()};
  }

  // 필터 조건
  private BooleanExpression employeeNumberCondition(String employeeNumber, QEmployeeHistory q) {
    return employeeNumber != null ? q.employeeNumber.containsIgnoreCase(employeeNumber) : null;
  }

  private BooleanExpression memoCondition(String memo, QEmployeeHistory q) {
    return memo != null ? q.memo.containsIgnoreCase(memo) : null;
  }

  private BooleanExpression ipAddressCondition(String ipAddress, QEmployeeHistory q) {
    return ipAddress != null ? q.ipAddress.containsIgnoreCase(ipAddress) : null;
  }

  private BooleanExpression changeTypeCondition(ChangeType changeType, QEmployeeHistory q) {
    return changeType != null ? q.changeType.eq(changeType) : null;
  }

  private BooleanExpression atFromCondition(Instant atFrom, QEmployeeHistory q) {
    return atFrom != null ? q.at.goe(atFrom) : null;
  }

  private BooleanExpression atToCondition(Instant atTo, QEmployeeHistory q) {
    return atTo != null ? q.at.loe(atTo) : null;
  }
}