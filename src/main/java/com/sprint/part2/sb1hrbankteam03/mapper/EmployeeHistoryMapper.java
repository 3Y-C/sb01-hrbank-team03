package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.ChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import org.springframework.stereotype.Component;

@Component
public class EmployeeHistoryMapper {

  public ChangeLogDto toDto(EmployeeHistory employeeHistory){
    return new ChangeLogDto(
        employeeHistory.getId(),
        employeeHistory.getChangeType(),
        employeeHistory.getEmployeeNumber(),
        employeeHistory.getMemo(),
        employeeHistory.getIpAddress(),
        employeeHistory.getAt()
    );
  }

}
