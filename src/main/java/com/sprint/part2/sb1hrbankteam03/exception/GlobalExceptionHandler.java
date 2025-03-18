package com.sprint.part2.sb1hrbankteam03.exception;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sprint.part2.sb1hrbankteam03.dto.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  //400 잘못된 요청
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalAccessException(IllegalArgumentException e) {
    ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.",
        e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

  //필수 요청 파라미터 없을 때(400)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParameterException(MissingServletRequestParameterException e){
    ErrorResponse errorResponse=ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "필수 요청 값이 없습니다.",e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  //유호성 검사
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
    String details=e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    ErrorResponse errorResponse=ErrorResponse.of(HttpStatus.BAD_REQUEST.value(),"입력값이 유효하지 않습니다.",details);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  //404
  @ExceptionHandler(ConfigDataResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ConfigDataResourceNotFoundException e){
    ErrorResponse errorResponse=ErrorResponse.of(HttpStatus.NOT_FOUND.value(), "요청한 리소스가 없습니다.",e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  //500
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleException(Exception e){
    ErrorResponse errorResponse=ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.",e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
