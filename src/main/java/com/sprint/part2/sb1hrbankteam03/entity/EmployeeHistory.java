package com.sprint.part2.sb1hrbankteam03.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;


@Entity
public class EmployeeHistory extends BaseEntity {

  String employeeNumber; //추가함!!

/*  @OneToMany(mappedBy ="employeeHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<EmployeeChangeDetail> getEmployeeChangeDetails = new ArrayList<>();*/

  @Enumerated(EnumType.STRING)
  ChangeType changeType;

  String description;
  String ipAddress;
  Instant editedAt;
}
