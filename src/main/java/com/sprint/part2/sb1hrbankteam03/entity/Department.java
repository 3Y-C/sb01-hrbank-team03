package com.sprint.part2.sb1hrbankteam03.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@NoArgsConstructor
public class Department extends BaseUpdatableEntity {

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "description",length = 255,nullable = true)
  private String description;

  @Column(name = "established_date",nullable = false)
  private LocalDate establishDate;

  public Department(String name, String description, LocalDate establishDate){
    this.name = name;
    this.description = description;
    this.establishDate = establishDate;
  }

  public void update(String newName, String newDescription, LocalDate newEstablishDate){
//    if(newName != null && !newName.equals(this.name)){
//      this.name = newName;
//    }
//    if(newDescription != null && !newDescription.equals(this.description)){
//      this.description = newDescription;
//    }
//    if(newEstablishDate != null && !newEstablishDate.equals(this.establishDate)) {
//      this.establishDate = newEstablishDate;
//    }
    this.name = newName;
    this.description = newDescription;
    this.establishDate = newEstablishDate;
  }

}
