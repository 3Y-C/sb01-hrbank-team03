package com.sprint.part2.sb1hrbankteam03.dto.department.respons;

import java.util.List;

public record CursorPageResponseDepartmentDto(
    List<DepartmentDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    Integer totalElements,
    boolean hasNext
) { }
