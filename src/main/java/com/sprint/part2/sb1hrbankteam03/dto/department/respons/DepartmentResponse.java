package com.sprint.part2.sb1hrbankteam03.dto.department.respons;

import java.time.LocalDate;

public record DepartmentResponse(
    Long id,
    String name,
    String description,
    LocalDate establishedDate,
    Long employeeCount
) { }
