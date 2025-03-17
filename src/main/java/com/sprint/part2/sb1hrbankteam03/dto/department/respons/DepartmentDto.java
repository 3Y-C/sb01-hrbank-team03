package com.sprint.part2.sb1hrbankteam03.dto.department.respons;

public record DepartmentDto(
    Long id,
    String name,
    String description,
    String establishedDate,
    Integer employeeCount
) { }
