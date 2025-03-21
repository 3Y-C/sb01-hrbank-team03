package com.sprint.part2.sb1hrbankteam03.config.api;

import com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.DiffDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeChangeInfo;
import com.sprint.part2.sb1hrbankteam03.entity.enums.ChangeType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "직원 정보 수정 이력 관리", description = "직원 정보 수정 이력 관리 API")
public interface EmployHistoryApi {

  @Operation(summary = "직원 정보 수정 이력 목록 조회", description = "직원 정보 수정 이력 목록을 조회합니다. 상세 변경 내용은 포함되지 않습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseChangeLogDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 지원하지 않는 정렬 필드",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(
                  value = "{\n" +
                      "  \"timestamp\": \"2025-03-06T05:39:06.152068Z\",\n" +
                      "  \"status\": 400,\n" +
                      "  \"message\": \"잘못된 요청입니다.\",\n" +
                      "  \"details\": \"요청 주소와 필드를 확인해주세요.\"\n" +
                      "}"
              )}
          )
      )})
  ResponseEntity<CursorPageResponseChangeLogDto> getChangeLogs(
      @Parameter(description = "대상 직원 사번")
      @RequestParam(value = "employeeNumber", required = false)
      String employeeNumber,
      @Parameter(description = "내용")
      @RequestParam(value = "memo", required = false) String memo,
      @Parameter(description = "ip주소")
      @RequestParam(value = "ipAddress", required = false)
      String ipAddress,
      @Parameter(description = "이력 유형 (CREATED, UPDATED, DELETED)")
      @RequestParam(value = "type", required = false)
      ChangeType changeType,
      @Parameter(description = "수정 일시(부터)")
      @RequestParam(value = "atFrom", required = false) Instant atFrom,
      @Parameter(description = "수정 일시(까지)")
      @RequestParam(value = "atTo", required = false) Instant atTo,
      @Parameter(description = "커서 (이전 페이지의 마지막 ID)")
      @RequestParam(value = "cursor", required = false) String cursor,
      @Parameter(description = "이전 페이지 마지막 요소 ID")
      @RequestParam(value = "idAfter", required = false) Long idAfter,
      @Parameter(description = "정렬 필드 (ipAddress, at)")
      @RequestParam(value = "sortField", defaultValue = "at") String sortField,
      @Parameter(description = "정렬 방향 (asc, desc)")
      @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
      @PageableDefault(size = 30) Pageable pageable);

  // 특정 이력의 변경 상세 내역 조회
  @Operation(summary = "직원 정보 수정 이력 상세 조회", description = "직원 정보 수정 이력의 상세 정보를 조회합니다. 변경 상세 내용이 포함됩니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = EmployeeChangeInfo.class))),
      @ApiResponse(responseCode = "404", description = "이력을 찾을 수 없음",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(
                  value = "{\n" +
                      "  \"timestamp\": \"2025-03-06T05:39:06.152068Z\",\n" +
                      "  \"status\": 404,\n" +
                      "  \"message\": \"해당 이력을 찾을 수 없습니다\",\n" +
                      "  \"details\": \n" +
                      "}"
              )}
          )
      )})
  ResponseEntity<List<DiffDto>> getChangeDetails(
      @Parameter(description = "이력 ID", required = true)
      @PathVariable
      Long id
  );


  // 최근 일주일 이력 건수 조회
  @Operation(summary = "수정 이력 건수 조회", description = "직원 정보 수정 이력 건수를 조회합니다. 파라미터를 제공하지 않으면 최근 일주일 데이터를 반환합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(contentSchema = Long.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 날짜 범위",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(
                  value = "{\n" +
                      "  \"timestamp\": \"2025-03-06T05:39:06.152068Z\",\n" +
                      "  \"status\": 400,\n" +
                      "  \"message\": \"잘못된 요청입니다.\",\n" +
                      "  \"details\": \"잘못된 요청 또는 유효하지 않은 날짜 범위입니다.\"\n" +
                      "}"
              )}
          )
      )})
  ResponseEntity<Long> getRecentChangeLogCount(
      @Parameter(description = "시작 일시 (기본값: 7일전)")
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      Instant fromDate,
      @Parameter(description = "종료 일시 (기본값: 현재)")
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      Instant toDate);
}
