package com.sprint.part2.sb1hrbankteam03.dto.department.request;

public record DepartmentGetRequest(
    String nameOrDescription,
    Long idAfter,
    String cursor,
    int size,
    String sortField,
    String sortDirection
) {}
