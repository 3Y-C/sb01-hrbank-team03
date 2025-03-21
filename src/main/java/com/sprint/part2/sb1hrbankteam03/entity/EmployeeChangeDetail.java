package com.sprint.part2.sb1hrbankteam03.entity;

import com.sprint.part2.sb1hrbankteam03.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "employee_change_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeChangeDetail extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "history_id")
  EmployeeHistory employeeHistory;

  @Column(name = "property_name")
  String propertyName;

  @Column(name = "before")
  String before;

  @Column(name = "after")
  String after;
}
