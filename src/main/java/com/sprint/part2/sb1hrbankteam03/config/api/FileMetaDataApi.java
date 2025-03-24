package com.sprint.part2.sb1hrbankteam03.config.api;

import com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "파일 관리", description = "파일 관리 API")
public interface FileMetaDataApi {

  @Operation(summary = "파일 다운로드", description = "파일을 다운로드합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "다운로드 성공",
          content = @Content(mediaType = "string")),
      @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = {@ExampleObject(
                  value = "{\n" +
                      "  \"timestamp\": \"2025-03-06T05:39:06.152068Z\",\n" +
                      "  \"status\": 404,\n" +
                      "  \"message\": \"파일을 찾을 수 없습니다.\",\n" +
                      "  \"details\": \n" +
                      "}"
              )}
          )
      )})
  ResponseEntity<?> download(
      @Parameter(description = "파일 ID", required = true)
      @PathVariable("fileMetaDataId") Long fileMetaDataId);
}
