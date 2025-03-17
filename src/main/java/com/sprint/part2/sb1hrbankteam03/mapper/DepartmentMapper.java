package com.sprint.part2.sb1hrbankteam03.mapper;

import com.sprint.part2.sb1hrbankteam03.dto.department.respons.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentDto;
import com.sprint.part2.sb1hrbankteam03.entity.Department;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
  // 정적 상수로 DateTimeFormatter 선언
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public DepartmentDto toDto(Department department, Integer employeeCount) {
    String establishedDate = department.getEstablishDate().format(DATE_FORMATTER);
    return new DepartmentDto(
        department.getId(),
        department.getName(),
        department.getDescription(),
        establishedDate,
        employeeCount
    );
  }


  public CursorPageResponseChangeLogDto toCursorPageResponseDto(List<DepartmentDto> departmentList,
      String nextCursor, Long nextIdAfter,int size, Integer totalElements, boolean hasNext){
    return new CursorPageResponseChangeLogDto(
        departmentList,
        nextCursor,
        nextIdAfter,
        size,
        totalElements,
        hasNext);
  }
}
