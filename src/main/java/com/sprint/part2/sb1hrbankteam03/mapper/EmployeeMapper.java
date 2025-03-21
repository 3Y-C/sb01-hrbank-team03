package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.employee.CursorPageResponseEmployeeDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeSnapshotDto;
import com.sprint.part2.sb1hrbankteam03.entity.Employee;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

  public EmployeeDto todto(Employee savedEmployee) {

    Long profileId = null;
    if (savedEmployee.getProfileImage() != null) {
      profileId = savedEmployee.getProfileImage().getId();
    }

    return new EmployeeDto(
        savedEmployee.getId(),
        savedEmployee.getName(),
        savedEmployee.getEmail(),
        savedEmployee.getEmployeeNumber(),
        savedEmployee.getDepartment().getId().toString(),
        savedEmployee.getDepartment().getName(),
        savedEmployee.getPosition(),
        savedEmployee.getHireDate().toString(),
        savedEmployee.getStatus().toString(),
        profileId
    );
  }

  public CursorPageResponseEmployeeDto toPageDto(List<EmployeeDto> employeeDtoList,
      String nextCursor, Long nextIdAfter, int size, Integer totalElements, boolean hasNext) {

    return new CursorPageResponseEmployeeDto(
        employeeDtoList,
        nextCursor,
        nextIdAfter,
        size,
        totalElements,
        hasNext
    );
  }

  public EmployeeSnapshotDto toSnapshotDto(Employee employee) {
    return new EmployeeSnapshotDto(
        employee.getEmployeeNumber(),
        employee.getName(),
        employee.getEmail(),
        employee.getDepartment().getName(),
        employee.getPosition(),
        employee.getHireDate().toString(),
        employee.getStatus().name()
    );
  }

}
