package com.sprint.part2.sb1hrbankteam03.config.api;

import com.sprint.part2.sb1hrbankteam03.dto.employee.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "직원 관리", description = "직원 관리 API")
public interface EmployeeApi {

  @Operation(summary = "직원 목록 조회", description = "필터 및 커서 기반 페이지네이션으로 사원 목록을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "사원 목록 조회 성공",
              content = @Content(schema = @Schema(implementation = CursorPageResponseEmployeeDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class)))
      })
  @GetMapping
  ResponseEntity<CursorPageResponseEmployeeDto> getEmployees(
      @Parameter(description = "이름 또는 이메일 (부분 일치)")
      @RequestParam(name = "nameOrEmail", required = false) String keyword,
      @Parameter(description = "부서명 (부분 일치)")
      @RequestParam(name = "departmentName", required = false) String department,
      @Parameter(description = "직책 (부분 일치)")
      @RequestParam(required = false) String position,
      @Parameter(description = "사원번호 (부분 일치)")
      @RequestParam(required = false) String employeeNumber,
      @Parameter(description = "입사일 시작")
      @RequestParam(name = "hireDateFrom", required = false) String startDate,
      @Parameter(description = "입사일 종료")
      @RequestParam(name = "hireDateTo", required = false) String endDate,
      @Parameter(description = "상태 (예: ACTIVE)")
      @RequestParam(required = false) String status,
      @Parameter(description = "정렬 필드")
      @RequestParam(required = false) String sortField,
      @Parameter(description = "정렬 방향 (asc 또는 desc)")
      @RequestParam(required = false) String sortDirection,
      @Parameter(description = "커서")
      @RequestParam(required = false) String cursor,
      @Parameter(description = "페이지 크기")
      @RequestParam(defaultValue = "10") int size
  );

  @Operation(summary = "직원 등록", description = "신규 직원을 등록합니다. 프로필 이미지 업로드 가능",
      responses = {
          @ApiResponse(responseCode = "201", description = "사원 등록 성공",
              content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 중복된 이메일",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class)))
      })
  @PostMapping(consumes = "multipart/form-data", produces = "application/json")
  ResponseEntity<EmployeeDto> createEmployee(
      @RequestPart("employee") EmployeeCreateRequest employeeCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      HttpServletRequest request
  );

  @Operation(summary = "직원 상세 조회", description = "직원 ID로 상세 정보를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "사원 조회 성공",
              content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
          @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class)))
      })
  @GetMapping("/{employeeId}")
  ResponseEntity<EmployeeDto> getEmployee(@PathVariable("employeeId") Long employeeId);

  @Operation(summary = "직원 삭제", description = "직원을 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "204", description = "사원 삭제 성공"),
          @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class)))
      })
  @DeleteMapping("/{employeeId}")
  ResponseEntity<Void> deleteEmployee(@PathVariable("employeeId") Long employeeId, HttpServletRequest request);

  @Operation(summary = "직원 수정", description = "직원의 정보를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "사원 수정 성공",
              content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 중복된 이메일",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "404", description = "직원 또는 부서를 찾을 수 없음",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class)))
      })
  @PatchMapping(value = "/{employeeId}", consumes = "multipart/form-data", produces = "application/json")
  ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable("employeeId") Long employeeId,
      @RequestPart("employee") EmployeeUpdateRequest employeeUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      HttpServletRequest request
  );

  @Operation(summary = "직원 추이 변동", description = "입사일 기준 트렌드를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "트렌드 조회 성공",
              content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeTrendDto.class)))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 지원하지 않는 시간 단위",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class)))
      })
  @GetMapping("/stats/trend")
  ResponseEntity<List<EmployeeTrendDto>> getEmployeeTrend(
      @RequestParam(required = false) String from,
      @RequestParam(required = false) String to,
      @RequestParam(required = false, defaultValue = "month") String unit
  );

  @Operation(summary = "직원 분포 통계", description = "부서 등 기준으로 사원 분포를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "분포 조회 성공",
              content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeDistributionDto.class)))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 지원하지 않는 그룹화 기준",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class)))
      })
  @GetMapping("/stats/distribution")
  ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(
      @RequestParam(required = false, defaultValue = "department") String groupBy,
      @RequestParam(required = false, defaultValue = "ACTIVE") String status
  );

  @Operation(
      summary = "사원 수 조회",
      description = "상태, 입사일, 부서, 직책 조건에 맞는 사원 수를 조회합니다.",
      parameters = {
          @Parameter(name = "status", description = "사원 상태 (예: ACTIVE, INACTIVE)", example = "ACTIVE"),
          @Parameter(name = "fromDate", description = "입사 시작일 (yyyy-MM-dd)", example = "2024-01-01"),
          @Parameter(name = "toDate", description = "입사 종료일 (yyyy-MM-dd)", example = "2025-01-01"),
          @Parameter(name = "department", description = "부서명 (예: 프론트, 백엔드)", example = "프론트"),
          @Parameter(name = "position", description = "직책 (예: 대리, 과장)", example = "대리")
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "사원 수 조회 성공",
              content = @Content(schema = @Schema(implementation = Long.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse.class)))
      }
  )
  @GetMapping("/count")
  ResponseEntity<Long> getEmployeeCount(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String fromDate,
      @RequestParam(required = false) String toDate,
      @RequestParam(required = false) String department,
      @RequestParam(required = false) String position
  );

}
