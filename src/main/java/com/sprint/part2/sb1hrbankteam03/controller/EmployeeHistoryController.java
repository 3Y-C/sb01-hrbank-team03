package com.sprint.part2.sb1hrbankteam03.controller;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.DiffDto;
import com.sprint.part2.sb1hrbankteam03.entity.enums.ChangeType;
import com.sprint.part2.sb1hrbankteam03.service.EmployeeHistoryService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class EmployeeHistoryController {

  private final EmployeeHistoryService employeeHistoryService;

  @GetMapping
  public ResponseEntity<CursorPageResponseChangeLogDto> getChangeLogs(
      @RequestParam(value = "employeeNumber", required = false) String employeeNumber,
      @RequestParam(value = "memo", required = false) String memo,
      @RequestParam(value = "ipAddress", required = false) String ipAddress,
      @RequestParam(value = "type", required = false) ChangeType changeType,
      @RequestParam(value = "atFrom", required = false) Instant atFrom,
      @RequestParam(value = "atTo", required = false) Instant atTo,
      @RequestParam(value = "cursor", required = false) String cursor,
      @RequestParam(value = "idAfter", required = false) Long idAfter,
      @RequestParam(value = "sortField", defaultValue = "at") String sortField,
      @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
      @PageableDefault(size = 30) Pageable pageable) {

    CursorPageResponseChangeLogDto changeLogs = employeeHistoryService.getChangeLogs(
        employeeNumber, memo, ipAddress, changeType, atFrom, atTo, cursor, idAfter, sortField, sortDirection, pageable
    );

    return ResponseEntity.status(HttpStatus.OK).body(changeLogs);
  }

  // 특정 이력의 변경 상세 내역 조회
  @GetMapping("/{id}/diffs")
  public ResponseEntity<List<DiffDto>> getChangeDetails(@PathVariable Long id) {
    List<DiffDto> details = employeeHistoryService.getChangeDetails(id);
    return ResponseEntity.ok(details);
  }

  // 최근 일주일 이력 건수 조회
  @GetMapping("/count")
  public ResponseEntity<Long> getRecentChangeLogCount(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate) {

    // 기본값 설정: fromDate는 7일 전, toDate는 현재 시간
    Instant start = (fromDate != null) ? fromDate : Instant.now().minus(7, ChronoUnit.DAYS);
    Instant end = (toDate != null) ? toDate : Instant.now();

    long count = employeeHistoryService.getChangeLogCount(start, end);
    return ResponseEntity.ok(count);
  }
}