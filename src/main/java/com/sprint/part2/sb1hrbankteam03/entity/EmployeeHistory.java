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


@Entity
@Getter
@Setter
@Table(name = "employee_historys")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeHistory extends BaseEntity {

  @Column(name = "emloyee_number",nullable = false)
  String employeeNumber;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  ChangeType changeType;

  @Column(name = "memo")
  String description;

  @Column(name = "ip_address",nullable = false)
  String ipAddress;

  @Column(name = "edited_at",nullable = false)
  Instant editedAt;
}
