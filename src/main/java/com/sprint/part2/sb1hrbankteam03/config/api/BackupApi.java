package com.sprint.part2.sb1hrbankteam03.config.api;

import com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse;
import com.sprint.part2.sb1hrbankteam03.dto.backup.BackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.backup.CursorPageResponseBackupDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "데이터 백업 관리", description = "데이터 백업 관리 API")
public interface BackupApi {

  @Operation(summary = "데이터 백업 목록 조회", description = "데이터 백업 이력(목록)을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseBackupDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(
                  value = "{\n" +
                      "  \"timestamp\": \"2025-03-06T05:39:06.152068Z\",\n" +
                      "  \"status\": 400,\n" +
                      "  \"message\": \"잘못된 요청입니다.\",\n" +
                      "  \"details\": \"요청 주소를 확인해주세요.\"\n" +
                      "}"
              )}
          )
      )})
  ResponseEntity<CursorPageResponseBackupDto> getBackups(
      @Parameter(description = "작업자 ip")
      @RequestParam(required = false)
      String worker,
      @Parameter(description = "상태 (IN_PROGRESS, COMPLETED, FAILED)")
      @RequestParam(required = false)
      String status,
      @Parameter(description = "시작 시간(부터)")
      @RequestParam(required = false)
      String startedAtFrom,
      @Parameter(description = "시작 시간(까지)")
      @RequestParam(required = false)
      String startedAtTo,
      @Parameter(description = "이전 페이지 마지막 요소 ID")
      @RequestParam(required = false, defaultValue = "0")
      long idAfter,
      @Parameter(description = "커서(이전 페이지의 마지막 ID")
      @RequestParam(required = false)
      String cursor,
      @Parameter(description = "페이지 크기")
      @RequestParam(required = false, defaultValue = "10")
      int size,
      @Parameter(description = "정렬 필드 (startedAt, endedAt, status)")
      @RequestParam(required = false, defaultValue = "startedAt")
      String sortField,
      @Parameter(description = "정렬 방향 (ASC, DESC 대소문자 구분 X)")
      @RequestParam(required = false, defaultValue = "DESC")
      String sortDirection
  );

  @Operation(summary = "데이터 백업 생성", description = "데이터 백업을 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "백업 생성 성공",
          content = @Content(schema = @Schema(implementation = BackupDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(
                  value = "{\n" +
                      "  \"timestamp\": \"2025-03-06T05:39:06.152068Z\",\n" +
                      "  \"status\": 400,\n" +
                      "  \"message\": \"잘못된 요청입니다.\",\n" +
                      "  \"details\": \"요청 주소를 확인해주세요.\"\n" +
                      "}"
              )}
          )),
      @ApiResponse(responseCode = "409", description = "이미 진행중인 백업이 있음",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(
                  value = "{\n" +
                      "  \"timestamp\": \"2025-03-06T05:39:06.152068Z\",\n" +
                      "  \"status\": 409,\n" +
                      "  \"message\": \"이미 진행중인 백업이 있습니다.\",\n" +
                      "  \"details\": \".\"\n" +
                      "}"
              )}
          ))
  })
  ResponseEntity<BackupDto> createBackup(HttpServletRequest request);

  @Operation(summary = "최근 백업 정보 조회", description = "지정된 상태의 가장 최근 백업 정보를 조회합니다. 상태를 지정하지 않으면 성공적으로 완료된(COMPLETED) 백업을 반환합니다.\n")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = BackupDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 상태값",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(
                  value = "{\n" +
                      "  \"timestamp\": \"2025-03-06T05:39:06.152068Z\",\n" +
                      "  \"status\": 400,\n" +
                      "  \"message\": \"잘못된 요청입니다.\",\n" +
                      "  \"details\": \"잘못된 요청 또는 유효하지 않은 상태값입니다.\"\n" +
                      "}"
              )}
          )
      )})
  BackupDto getLatestBackup(
      @Parameter(description = "백업 상태 (COMPLETED, FAILED, IN_PROGRESS, 기본값: COMPLETED)")
      @RequestParam(required = false, defaultValue = "COMPLETED")
      String status
  );


}
