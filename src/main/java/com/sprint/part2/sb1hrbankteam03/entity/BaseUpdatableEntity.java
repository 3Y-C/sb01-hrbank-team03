package com.sprint.part2.sb1hrbankteam03.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.springframework.data.annotation.LastModifiedDate;


@MappedSuperclass
public abstract class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  Instant updatedAt;
}
