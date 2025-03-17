package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDto;
import com.sprint.part2.sb1hrbankteam03.entity.employee.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

  public EmployeeDto todto(Employee savedEmployee) {
    return new EmployeeDto(
        savedEmployee.getId(),
        savedEmployee.getName(),
        savedEmployee.getEmail(),
        savedEmployee.getEmployeeNumber(),
        //savedEmployee.getDepartment().getId().toString(),
        //savedEmployee.getDepartment().getName(),
        null,
        null,
        savedEmployee.getPosition(),
        savedEmployee.getHireDate().toString(),
        savedEmployee.getStatus().toString(),
        null
    );
  }
}
