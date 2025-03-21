package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.employee.CursorPageResponseEmployeeDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeSnapshotDto;
import com.sprint.part2.sb1hrbankteam03.entity.Employee;
import java.util.List;
import org.springframework.data.domain.Slice;
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


  public CursorPageResponseEmployeeDto fromSlice(
      Slice<EmployeeDto> slice, String sortField, int totalElements) {

    String nextCursor = null;
    Long nextIdAfter = null;

    if (!slice.getContent().isEmpty() && slice.hasNext()) {
      EmployeeDto last = slice.getContent().get(slice.getContent().size() - 1);

      switch (sortField) {
        case "name":
          nextCursor = last.getName();
          break;
        case "hireDate":
          nextCursor = last.getHireDate(); // 이미 String 형식
          break;
        default:
          nextCursor = String.valueOf(last.getId());
          break;
      }

      nextIdAfter = last.getId();
    }

    return new CursorPageResponseEmployeeDto(
        slice.getContent(),
        nextCursor,
        nextIdAfter,
        slice.getSize(),
        totalElements,
        slice.hasNext()
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
