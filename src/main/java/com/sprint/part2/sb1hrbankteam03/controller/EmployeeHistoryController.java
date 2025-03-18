package com.sprint.part2.sb1hrbankteam03.controller;

import com.sprint.part2.sb1hrbankteam03.dto.PageResponse;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.ChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.DiffDto;
import com.sprint.part2.sb1hrbankteam03.entity.ChangeType;
import com.sprint.part2.sb1hrbankteam03.service.EmployeeHistoryService;
import java.time.Instant;
import java.time.LocalDateTime;
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

  // 이력 목록 조회 (검색 & 필터링 & 정렬 & 페이지네이션)
/*  @GetMapping
  public ResponseEntity<Page<ChangeLogDto>> getChangeLogs(
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) String memo,
      @RequestParam(required = false) String ipAddress,
      @RequestParam(required = false) ChangeType changeType,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "editedAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    Page<ChangeLogDto> historyList = employeeHistoryService.getChangeLogs(
        employeeNumber, memo, ipAddress, changeType, startTime, endTime, page, size, sortBy, sortDirection);

    return ResponseEntity.ok(historyList);
  }*/

  @GetMapping("/api/change-logs")
  public ResponseEntity<PageResponse<ChangeLogDto>> getChangeLogs(
      @RequestParam(value = "employeeNumber", required = false) String employeeNumber,
      @RequestParam(value = "memo", required = false) String memo,
      @RequestParam(value = "ipAddress", required = false) String ipAddress,
      @RequestParam(value = "type", required = false) ChangeType changeType,
      @RequestParam(value = "atFrom", required = false) Instant atFrom,
      @RequestParam(value = "atTo", required = false) Instant atTo,
      @RequestParam(value = "cursor", required = false) Instant cursor,
      @RequestParam(value = "sortField", defaultValue = "at") String sortField,
      @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
      @PageableDefault(size = 10) Pageable pageable) {

    PageResponse<ChangeLogDto> changeLogs = employeeHistoryService.getChangeLogs(
        employeeNumber, memo, ipAddress, changeType, atFrom, atTo, cursor, sortField, sortDirection, pageable
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
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

    // 기본값 설정: fromDate는 7일 전, toDate는 현재 시간
    LocalDateTime start = (fromDate != null) ? fromDate : LocalDateTime.now().minusDays(7);
    LocalDateTime end = (toDate != null) ? toDate : LocalDateTime.now();

    long count = employeeHistoryService.getChangeLogCount(start, end);
    return ResponseEntity.ok(count);
  }
}