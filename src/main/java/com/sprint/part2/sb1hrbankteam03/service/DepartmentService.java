package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentListResponse;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentResponse;

public interface DepartmentService {
  DepartmentResponse create(DepartmentCreateRequest departmentCreateRequest);

  DepartmentResponse update(Long id, DepartmentUpdateRequest departmentUpdateRequest);

  DepartmentListResponse findDepartments(String nameOrDescription, Long idAfter,
      String cursor, int size, String sortField,
      String sortDirection);
}
