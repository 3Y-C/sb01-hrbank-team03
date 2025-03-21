package com.sprint.part2.sb1hrbankteam03.config.api;

import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.CursorPageResponseDepartmentDto;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Department", description = "부서 관련 API")
public interface DepartmentApi {

  @Operation(summary = "부서 생성", description = "새로운 부서를 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "부서 생성 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class,
              example = "{\n  \"id\": 1,\n  \"name\": \"개발팀\",\n  \"description\": \"소프트웨어 개발을 담당하는 부서입니다.\",\n  \"establishedDate\": \"2023-01-01\",\n  \"employeeCount\": 10\n}"))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @PostMapping
  ResponseEntity<DepartmentDto> create(
      @Parameter(description = "생성할 부서 정보", required = true)
      @RequestBody DepartmentCreateRequest departmentCreateRequest
  );

  @Operation(summary = "부서 수정", description = "특정 부서 정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "부서 수정 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음")
  })
  @PatchMapping("/{id}")
  ResponseEntity<DepartmentDto> update(
      @Parameter(description = "수정할 부서 ID", required = true)
      @PathVariable Long id,
      @RequestBody DepartmentUpdateRequest departmentUpdateRequest
  );

  @Operation(summary = "부서 목록 조회", description = "검색 조건을 바탕으로 부서 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "부서 목록 조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseDepartmentDto.class)))
  })
  @GetMapping
  ResponseEntity<CursorPageResponseDepartmentDto> findDepartments(
      @Parameter(description = "부서 이름 또는 설명") @RequestParam(required = false, defaultValue = "") String nameOrDescription,
      @Parameter(description = "커서 기반 페이지네이션을 위한 ID") @RequestParam(required = false, defaultValue = "0") Long idAfter,
      @Parameter(description = "페이징 커서") @RequestParam(required = false, defaultValue = "") String cursor,
      @Parameter(description = "페이지 크기") @RequestParam(required = false, defaultValue = "20") int size,
      @Parameter(description = "정렬 필드") @RequestParam(required = false, defaultValue = "name") String sortField,
      @Parameter(description = "정렬 방향 (asc/desc)") @RequestParam(required = false, defaultValue = "asc") String sortDirection
  );

  @Operation(summary = "부서 삭제", description = "특정 부서를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "부서 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 부서 ID", required = true)
      @PathVariable Long id
  );

  @Operation(summary = "부서 상세 조회", description = "특정 부서의 상세 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "부서 조회 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음")
  })
  @GetMapping("/{id}")
  ResponseEntity<DepartmentDto> findById(
      @Parameter(description = "조회할 부서 ID", required = true)
      @PathVariable Long id
  );
}
