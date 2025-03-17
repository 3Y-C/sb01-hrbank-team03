package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentListResponse;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentResponse;
import com.sprint.part2.sb1hrbankteam03.entity.Department;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
  public DepartmentResponse toDto(Department department, Long employeeCount) {
    return new DepartmentResponse(
        department.getId(),
        department.getName(),
        department.getDescription(),
        department.getEstablishDate(),
        employeeCount
    );
  }


  public DepartmentListResponse toListDto(List<DepartmentResponse> departmentList,
      String nextCursor, Long nextIdAfter,int size, long totalElements, boolean hasNext){
    return new DepartmentListResponse(
        departmentList,
        nextCursor,
        nextIdAfter,
        size,
        totalElements,
        hasNext);
  }
}
