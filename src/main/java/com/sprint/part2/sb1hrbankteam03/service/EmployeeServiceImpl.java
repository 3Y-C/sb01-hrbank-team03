package com.sprint.part2.sb1hrbankteam03.service;

import static com.sprint.part2.sb1hrbankteam03.entity.Status.ACTIVE;

import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDistributionDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeTrendDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.entity.Employee;
import com.sprint.part2.sb1hrbankteam03.entity.Department;
import com.sprint.part2.sb1hrbankteam03.entity.FileMetaData;
import com.sprint.part2.sb1hrbankteam03.entity.Status;
import com.sprint.part2.sb1hrbankteam03.mapper.EmployeeMapper;
import com.sprint.part2.sb1hrbankteam03.repository.DepartmentRepository;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeRepository;
import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeeMapper employeeMapper;
  //private final FileStorageService fileStorageService;

  @Override
  public List<EmployeeDto> getEmployees(String keyword, String department, String position,
      String employeeNumber, String startDate, String endDate,
      String status,String sortField, String sortDirection,String cursor,int size) {
    Status employeeStatus = (status != null) ? Status.from(status) : null;

    Pageable pageable = (Pageable) PageRequest.of(0, size,getSort(sortField,sortDirection));
    Long idAfter=(cursor!=null)?Long.parseLong(cursor):null;


    // 날짜 변환 시 예외 처리 추가
    LocalDate start = null;
    LocalDate end = null;
    try {
      start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
      end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("입사일 형식이 올바르지 않습니다. (예: yyyy-MM-dd)");
    }


    List<Employee> employees = employeeRepository.findEmployeesWithCursor(keyword, department, position,
        employeeNumber, start, end, employeeStatus,idAfter,pageable);

    return employees.stream().map(employeeMapper::todto).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public EmployeeDto createEmployee(EmployeeCreateRequest request,
      MultipartFile profile) {
    validateEmailUnique(request.getEmail());

    Department department = departmentRepository.findById(request.getDepartmentId())
        .orElseThrow(()->new IllegalArgumentException("부서를 찾을 수 없습니다."));

    LocalDate parsedHireDate=validateAndParseDate(request.getHireDate());
    String employeeNumber=generateEmployeeNumber(String.valueOf(request.getHireDate()));

    FileMetaData profileImage=null;

    Employee employee=new Employee(
        request.getName(),
        request.getEmail(),
        employeeNumber,
        department,
        request.getPosition(),
        parsedHireDate,
        ACTIVE,
        profileImage
    );
    Employee savedEmployee = employeeRepository.save(employee);
    return employeeMapper.todto(savedEmployee);
  }


  @Override
  public EmployeeDto getEmployeeById(Long employeeId) {
    return employeeRepository.findById(employeeId)
        .map(employeeMapper::todto)
        .orElseThrow(()->new IllegalArgumentException("Employee id: " + employeeId + " not found."));
  }

  @Override
  @Transactional
  public void deleteEmployee(Long employeeId) {
    Employee employee=employeeRepository.findById(employeeId)
        .orElseThrow(()->new IllegalArgumentException("직원을 찾을 수 없습니다."));
    if(employee.getStatus().equals(Status.RESIGNED)){
      throw new IllegalArgumentException("이미 퇴사한 직원");
    }
    //프로필 이미지 삭제 구현
    employee.setStatus("RESIGNED");
  }


  @Override
  @Transactional
  public EmployeeDto updateEmployee(Long employeeId, EmployeeUpdateRequest request,
      MultipartFile profile) {
    Employee employee=employeeRepository.findById(employeeId)
        .orElseThrow(()->new IllegalArgumentException("직원을 찾을 수 없습니다."));

    if (!employee.getEmail().equals(request.getEmail())) {
      validateEmailUnique(request.getEmail());
    }

    Department department=departmentRepository.findById(request.getDepartmentId())
        .orElseThrow(()->new IllegalArgumentException("부서를 찾을 수 없습니다."));

    /*if (profile!=null) {
      if (employee.getProfileImage()!=null) {
      }
    }*/

    LocalDate parsedHireDate = validateAndParseDate(request.getHireDate());

    employee.update(request,department,parsedHireDate);

    return employeeMapper.todto(employee);
  }

  @Override
  public List<EmployeeTrendDto> getAllEmployTrend(String startDate, String endDate, String unit) {
    LocalDate now=LocalDate.now();

    LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : getDefaultStartDate(now, unit);
    LocalDate end=(endDate != null) ? LocalDate.parse(endDate) : now;

    String dateFormat = getDateFormat(unit);
    List<Object[]> employeeTrend = employeeRepository.getEmployeeTrend(start, end,
        dateFormat);
    return employeeTrend.stream().map(row->new EmployeeTrendDto(
        (String) row[0],
        ((Number)row[1]).longValue(),
        ((Number)row[2]).longValue(),
        ((Number)row[3]).longValue()
    )).collect(Collectors.toList());
  }


  @Override
  public List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy, String status) {
    String criteria =(groupBy != null)? groupBy.toLowerCase():"department";
    Status employeeStatus=(status !=null)? Status.from(status):ACTIVE;
    List<Object[]> data = employeeRepository.getEmployeeDistribution(criteria, employeeStatus);

    long totalCount=employeeRepository.countByStatus(employeeStatus);
    List<EmployeeDistributionDto> distribution = data.stream()
        .map(objects -> {
          String groupKey = (String) objects[0];
          int count = ((Number) objects[1]).intValue();
          double percentage = (totalCount > 0) ? (count * 100.0 / totalCount) : 0.0;

          return new EmployeeDistributionDto(groupKey, count, percentage);
        })
        .toList();
    return distribution;
  }

  @Override
  public long getTotalEmployeeCount(String status, String fromDate, String toDate) {
    Status employeeStatus=(status!=null) ? Status.from(status) : null;

    LocalDate now=LocalDate.now();
    LocalDate start = (fromDate != null) ? LocalDate.parse(fromDate) : null;
    LocalDate end = (toDate != null) ? LocalDate.parse(toDate) : now;

    if(start!=null && end!=null) {
      return employeeRepository.countByStatusAndHireDateBetween(employeeStatus,start,end);
    }else if (start!=null) {
      return employeeRepository.countByStatusAndHireDateAfter(employeeStatus,start);
    }else{
      return employeeRepository.countByStatus(employeeStatus);
    }
  }


  private void validateEmailUnique(String email) {
    if (employeeRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }
  }

  private String generateEmployeeNumber(String hireDate) {
    String formattedDate=hireDate.replace("-","");
    String uuid= UUID.randomUUID().toString().replace("-","");
    return formattedDate+"-"+uuid;
  }

  private LocalDate validateAndParseDate(String hireDate) {
    try {
      return LocalDate.parse(hireDate, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다. (yyyy-MM-dd 형식 필요)");
    }
  }

  private LocalDate getDefaultStartDate(LocalDate now, String unit) {
    switch (unit.toLowerCase()) {
      case "day":
        return now.minusDays(12);
      case "week":
        return now.minusWeeks(12);
      case "quarter":
        return now.minusMonths(36);
      case "year":
        return now.minusYears(12);
      default:
        return now.minusMonths(12);
    }
  }
  private String getDateFormat(String unit) {
    switch (unit.toLowerCase()) {
      case "day":
        return "YYYY-MM-DD";
      case "week":
        return "IYYY-IW"; // ISO Week
      case "month":
        return "YYYY-MM";
      case "quarter":
        return "YYYY-Q";
      case "year":
        return "YYYY";
      default:
        return "YYYY-MM";
    }  }

  private Sort getSort(String sortField, String sortDirection) {
    Sort.Direction direction =sortDirection.equalsIgnoreCase("desc")?Sort.Direction.DESC:Sort.Direction.ASC;
    return switch (sortField.toLowerCase()){
      case "name" -> Sort.by(direction,"name");
      case "employeeNumber" -> Sort.by(direction,"employeeNumber");
      case "hireDate" -> Sort.by(direction,"hireDate");
      default -> Sort.by(Sort.Direction.ASC,"name");
    };
  }
}
