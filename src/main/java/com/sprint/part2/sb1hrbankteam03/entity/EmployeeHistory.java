package com.sprint.part2.sb1hrbankteam03.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Getter
@Setter
@Table(name = "employee_historys")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeHistory extends BaseEntity {

  @Column(name = "employee_number",nullable = false)
  String employeeNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, name = "type", columnDefinition = "history_type_enum")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  ChangeType changeType;

  @Column(name = "memo")
  String memo; //description > memo

  @Column(name = "ip_address",nullable = false)
  String ipAddress;

  @Column(name = "edited_at",nullable = false)
  Instant at; //editedAt > at
}
