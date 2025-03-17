package com.sprint.part2.sb1hrbankteam03.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeHistory extends BaseEntity {

  String employeeNumber;

  @Enumerated(EnumType.STRING)
  ChangeType changeType;

  String description;
  String ipAddress;
  Instant editedAt;
}
