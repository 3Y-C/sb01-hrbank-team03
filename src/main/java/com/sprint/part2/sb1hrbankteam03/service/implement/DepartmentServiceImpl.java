package com.sprint.part2.sb1hrbankteam03.service.implement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.CursorPageResponseDepartmentDto;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentDto;
import com.sprint.part2.sb1hrbankteam03.entity.Department;
import com.sprint.part2.sb1hrbankteam03.mapper.DepartmentMapper;
import com.sprint.part2.sb1hrbankteam03.repository.DepartmentRepository;
import com.sprint.part2.sb1hrbankteam03.service.DepartmentService;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;

  @Transactional
  public DepartmentDto create(DepartmentCreateRequest departmentCreateRequest){
    //빈 문자열, 공백만 포함된 문자열인지 검사
    if (!StringUtils.hasText(departmentCreateRequest.name())) {
      throw new IllegalArgumentException("Department name cannot be null or empty");
    }

    // 같은 이름의 부서가 이미 존재하는지 확인
    boolean departmentExists = departmentRepository.existsByName(departmentCreateRequest.name());
    if (departmentExists) {
      throw new IllegalArgumentException("Department with this name already exists");
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

  @Transactional
  public void delete(Long id){
    departmentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("department with id"  + id + "not found"));
    Integer employeeCount = departmentRepository.countEmployeesByDepartmentId(id);
    if(employeeCount > 0){
      throw new IllegalStateException("직원이 있는 부서는 삭제할 수 없습니다.");
    }
    departmentRepository.deleteById(id);
  }

  @Transactional
  public DepartmentDto findById(Long id){
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("department with id"  + id + "not found"));
    // 직원 수 조회
    Integer employeeCount = departmentRepository.countEmployeesByDepartmentId(id);

    return departmentMapper.toDto(department, employeeCount);
  }




  @Transactional(readOnly = true)
  public CursorPageResponseDepartmentDto findDepartments(String nameOrDescription, Long idAfter,
      String cursor, int size, String sortField,
      String sortDirection) {

    // 정렬 방향 설정
    Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

    // 정렬 필드
    String validSortField = sortField.equals("name") ? "name" : "established_date";

    Long startId = null;
    if (cursor != null && !cursor.isEmpty()) {
      try {
        // Base64 디코딩
        String decodedCursor = new String(Base64.getDecoder().decode(cursor));

        // JSON 파싱을 위해 Jackson ObjectMapper 사용
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(decodedCursor);

        // 'id' 값을 Long으로 추출
        startId = jsonNode.get("id").asLong();  // "id" 필드 값을 Long으로 추출
      } catch (Exception e) {
        throw new IllegalArgumentException("잘못된 커서 형식입니다.", e);
      }
    }


    Pageable pageable = PageRequest.of(0, size, Sort.by(direction, validSortField));
    Page<Department> departments = null;
    Department departmentt;
    if(cursor.equals("")){
      departments = departmentRepository.searchDepartments(nameOrDescription,startId,pageable);
    }else{
      departmentt = departmentRepository.findById(startId)
          .orElseThrow(() -> new NoSuchElementException("Department with id not found"));
      if(validSortField == "name"){
        String startName = departmentt.getName();
        if(direction == Direction.ASC){
          departments= departmentRepository.searchDepartmentsByNameAsc(nameOrDescription,startName,pageable);
        }else{
          departments= departmentRepository.searchDepartmentsByNameDesc(nameOrDescription,startName,pageable);
        }
      }else{
        LocalDate startData = departmentt.getEstablished_date();
        if(direction == Direction.ASC){

          departments= departmentRepository.searchDepartmentsByDateAscNativeASC(nameOrDescription,startData,pageable);
        }else{
          departments= departmentRepository.searchDepartmentsByDateAscNativeDesc(nameOrDescription,startData,pageable);
        }
      }
    }


    int currentPageSize = departments.getContent().size();

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

    Integer totalDepartments = departmentRepository.countDepartmentsByCondition(nameOrDescription);

    return departmentMapper.toCursorPageResponseDto(departmentList,
        nextCursor,
        nextIdAfter,
        currentPageSize,
        totalDepartments,
        departments.hasNext());
  }
}
