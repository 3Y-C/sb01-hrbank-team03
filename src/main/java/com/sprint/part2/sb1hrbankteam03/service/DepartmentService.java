package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentDto;
import java.util.Map;

public interface DepartmentService {
  DepartmentDto create(DepartmentCreateRequest departmentCreateRequest);

  DepartmentDto update(Long id, DepartmentUpdateRequest departmentUpdateRequest);

  CursorPageResponseChangeLogDto findDepartments(String nameOrDescription, Long idAfter,
      String cursor, int size, String sortField,
      String sortDirection);

//  Map<String, Object> findDepartments(String nameOrDescription, Long idAfter,
//      String cursor, int size, String sortField,
//      String sortDirection);
}
