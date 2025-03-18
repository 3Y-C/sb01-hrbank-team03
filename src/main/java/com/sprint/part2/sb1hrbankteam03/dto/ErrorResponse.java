package com.sprint.part2.sb1hrbankteam03.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  @JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
  private LocalDateTime timestamp;

  private int status;
  private String message;
  private String details;

  public static ErrorResponse of(int status, String message, String details) {
    return new ErrorResponse(LocalDateTime.now(), status, message, details);
  }
}
