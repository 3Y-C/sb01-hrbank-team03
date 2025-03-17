package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentDto;
import com.sprint.part2.sb1hrbankteam03.entity.Department;
import com.sprint.part2.sb1hrbankteam03.mapper.DepartmentMapper;
import com.sprint.part2.sb1hrbankteam03.repository.DepartmentRepository;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService{

  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;

  @Transactional
  public DepartmentDto create(DepartmentCreateRequest departmentCreateRequest){
    //빈 문자열, 공백만 포함된 문자열인지 검사
    if (!StringUtils.hasText(departmentCreateRequest.name())) {
      throw new IllegalArgumentException("Department name cannot be null or empty");
    }
    LocalDate establishDate = LocalDate.parse(departmentCreateRequest.establishedDate());
    Department department =
        new Department(departmentCreateRequest.name(), departmentCreateRequest.description(),
            establishDate);
    departmentRepository.save(department);
    Integer count = departmentRepository.countEmployeesByDepartmentId(department.getId());
    return departmentMapper.toDto(department, count);
  }

  @Transactional
  public DepartmentDto update(Long id,DepartmentUpdateRequest departmentUpdateRequest){
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("department with id"  + id + "not found"));
    if(departmentRepository.existsByName(departmentUpdateRequest.name())){
      throw new IllegalArgumentException("이미 존재하는 부서 이름입니다: " + departmentUpdateRequest.name());
    }
    department.update(departmentUpdateRequest.name(), departmentUpdateRequest.description(),
        departmentUpdateRequest.establishedDate());
    Integer count = departmentRepository.countEmployeesByDepartmentId(department.getId());
    return departmentMapper.toDto(department, count);
  }



  @Transactional(readOnly = true)
  public CursorPageResponseChangeLogDto findDepartments(String nameOrDescription, Long idAfter,
      String cursor, int size, String sortField,
      String sortDirection) {

    // 정렬 방향 설정
    Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

    // 정렬 필드
    String validSortField = sortField.equals("name") ? "name" : "establishDate";

    Long startId = null;
    if (cursor != null) {
      String decodedCursor = new String(Base64.getDecoder().decode(cursor));
      startId = Long.valueOf(decodedCursor); // 커서에서 ID를 추출
    }

    Pageable pageable = PageRequest.of(0, size, Sort.by(direction, validSortField));

    //부서 조회
    Page<Department> departments = departmentRepository.searchDepartments(nameOrDescription, startId, pageable);


    // 다음 페이지를 위한 커서 설정 base64 인코딩, 디코딩으로
    // "{"id":" 인코딩하면 "eyJpZCI6" 나오고, "}" 디코딩하면 "fQ==" 값이 나온다.
    String nextCursor = departments.hasNext()
        ? Base64.getEncoder().encodeToString(
        ("{\"id\":" + departments.getContent().get(departments.getSize() - 1).getId() + "}").getBytes()) : null;

    Long nextIdAfter = departments.hasNext() ? departments.getContent().get(departments.getSize() - 1).getId() : null;

    List<DepartmentDto> departmentList = departments.getContent().stream()
        .map(department -> {
          Integer employeeCount = departmentRepository.countEmployeesByDepartmentId(department.getId());
          return departmentMapper.toDto(department, employeeCount);
        })
        .collect(Collectors.toList());

    Integer totalDepartments = departmentRepository.countDepartments();

    return departmentMapper.toCursorPageResponseDto(departmentList,
        nextCursor,
        nextIdAfter,
        size,
        totalDepartments,
        departments.hasNext());
  }
}
