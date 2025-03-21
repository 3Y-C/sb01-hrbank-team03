package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.CursorPageResponseDepartmentDto;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentDto;

public interface DepartmentService {
  DepartmentDto create(DepartmentCreateRequest departmentCreateRequest);

  DepartmentDto update(Long id, DepartmentUpdateRequest departmentUpdateRequest);

  CursorPageResponseDepartmentDto findDepartments(String nameOrDescription, Long idAfter,
      String cursor, int size, String sortField,
      String sortDirection);

  void delete(Long id);

  DepartmentDto findById(Long id);
}
