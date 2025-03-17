package com.sprint.part2.sb1hrbankteam03.dto.department.request;

import java.time.LocalDate;

public record DepartmentUpdateRequest(
    String name,
    String description,
    LocalDate establishedDate
) { }
