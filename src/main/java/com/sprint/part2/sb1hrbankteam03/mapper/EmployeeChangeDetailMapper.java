package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.DiffDto;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeChangeDetail;
import org.springframework.stereotype.Component;

@Component
public class EmployeeChangeDetailMapper {

  public DiffDto toDto(EmployeeChangeDetail employeeChangeDetail){
    return new DiffDto(
        employeeChangeDetail.getPropertyName(),
        employeeChangeDetail.getBefore(),
        employeeChangeDetail.getAfter()
    );
  }
}
