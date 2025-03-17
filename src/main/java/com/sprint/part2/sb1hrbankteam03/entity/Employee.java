package com.sprint.part2.sb1hrbankteam03.entity;

import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseUpdatableEntity {

  @Column(length = 100,nullable = false)
  private String name;
  @Column(length = 255,nullable = false,unique = true)
  private String email;
  @Column(length = 255, nullable = false,unique = true)
  private String employeeNumber;

  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;
  @Column(length = 100,nullable = false)
  private String position;
  @Column(nullable = false)
  private LocalDate hireDate;


 // @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private String status;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_id")
  private FileMetaData profileImage;

  public void update(EmployeeUpdateRequest request,Department department,LocalDate hireDate) {
    this.name = request.getName();
    this.email = request.getEmail();
    this.department = department;
    this.position = request.getPosition();
    this.hireDate = hireDate;
    this.status=request.getStatus().toUpperCase();
  }

  public void setProfileImage(FileMetaData profileImage) {
    this.profileImage = profileImage;
  }
  public void setStatus(String status) {this.status = status.toUpperCase();}
}
