package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDistributionDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeTrendDto;
import com.sprint.part2.sb1hrbankteam03.dto.employee.EmployeeUpdateRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
  List<EmployeeDto> getEmployees(String keyword, String department, String position,
      String employeeNumber, String startDate, String endDate,
      String status,String sortField, String sortDirection,String cursor,int size);
  EmployeeDto createEmployee(EmployeeCreateRequest request,
      MultipartFile profile);
  EmployeeDto getEmployeeById(Long employeeId);
  EmployeeDto updateEmployee(Long employeeId, EmployeeUpdateRequest request,
      MultipartFile profile);
  void deleteEmployee(Long employeeId);
  List<EmployeeTrendDto> getAllEmployTrend(String startDate, String endDate, String unit);
  List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy, String status);
  long getTotalEmployeeCount(String status, String fromDate, String toDate);
}
