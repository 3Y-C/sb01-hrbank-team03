package com.sprint.part2.sb1hrbankteam03.controller;

import com.sprint.part2.sb1hrbankteam03.dto.employee.CursorPageResponseEmployeeDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDistributionDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeTrendDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.service.EmployeeServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeServiceImpl employeeService;

  @GetMapping
  public ResponseEntity<CursorPageResponseEmployeeDto> getEmployees(
      @RequestParam(required = false) String keyword,  // 이름 또는 이메일 (부분 일치)
      @RequestParam(required = false) String department,  // 부서 (부분 일치)
      @RequestParam(required = false) String position,  // 직함 (부분 일치)
      @RequestParam(required = false) String employeeNumber,  // 사원번호 (부분 일치)
      @RequestParam(required = false) String startDate,  // 입사일 범위 시작
      @RequestParam(required = false) String endDate,  // 입사일 범위 끝
      @RequestParam(required = false) String status, // 상태 (완전 일치)
      @RequestParam(required = false) String sortField,
      @RequestParam(required = false) String sortDirection,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size

  ) {
    if(sortDirection==null||sortDirection.isEmpty()){
      sortDirection="asc";
    }
    CursorPageResponseEmployeeDto employees = employeeService.getEmployees(keyword, department, position,
        employeeNumber, startDate, endDate, status,sortField,sortDirection,cursor,size);

    return ResponseEntity.ok(employees);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeDto> createEmployee(@RequestPart(value = "employeeCreateRequest",required = false) EmployeeCreateRequest employeeCreateRequest,
      @RequestPart(value = "profile",required = false) MultipartFile profile) {
    EmployeeDto createEmployee=employeeService.createEmployee(employeeCreateRequest,profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(createEmployee);
  }

  @GetMapping(value = "/{employeeId}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("employeeId") Long employeeId) {
    return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
  }

  @DeleteMapping(value = "/{employeeId}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
    employeeService.deleteEmployee(employeeId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PatchMapping(value = "/{employeeId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable("employeeId") Long employeeId,
      @RequestPart(value = "employeeUpdateRequest") EmployeeUpdateRequest employeeUpdateRequest,
      @RequestPart(value = "profile",required = false) MultipartFile profile) {
    EmployeeDto updateEmployee=employeeService.updateEmployee(employeeId,employeeUpdateRequest,profile);
    return ResponseEntity.ok(updateEmployee);
  }

  @GetMapping("/stats/trend")
  public ResponseEntity<List<EmployeeTrendDto>> getEmployeeTrend(
      @RequestParam(required = false) String from,
      @RequestParam(required = false) String to,
      @RequestParam(required = false,defaultValue = "month") String unit
  ) {
    List<EmployeeTrendDto> trends=employeeService.getAllEmployTrend(from,to,unit);
    return ResponseEntity.ok(trends);
  }



  @GetMapping("/stats/distribution")
  public ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(
          @RequestParam(required = false,defaultValue = "department") String groupBy,
      @RequestParam(required = false,defaultValue = "ACTIVE") String status) {
    List<EmployeeDistributionDto> distribution=employeeService.getEmployeeDistribution(groupBy,status);
    return ResponseEntity.ok(distribution);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getEmployeeCount(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String fromDate,
      @RequestParam(required = false) String toDate
  ) {
    long count=employeeService.getTotalEmployeeCount(status,fromDate,toDate);
    return ResponseEntity.ok(count);
  }

}
