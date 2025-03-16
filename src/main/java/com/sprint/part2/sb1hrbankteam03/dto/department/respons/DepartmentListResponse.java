package com.sprint.part2.sb1hrbankteam03.dto.department.respons;

import java.util.List;

public record DepartmentListResponse(
    List<DepartmentResponse> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    long totalElements,
    boolean hasNext
) { }
