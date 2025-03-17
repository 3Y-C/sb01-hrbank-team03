package com.sprint.part2.sb1hrbankteam03.dto.department.request;

import java.time.LocalDate;

public record DepartmentCreateRequest(
  String name,
  String description,
  String establishedDate
){ }
