package com.sprint.part2.sb1hrbankteam03.controller;

import com.sprint.part2.sb1hrbankteam03.common.util.IpUtils;
import com.sprint.part2.sb1hrbankteam03.config.api.BackupApi;
import com.sprint.part2.sb1hrbankteam03.dto.backup.BackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.backup.CursorPageResponseBackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.backup.RequestBackupDto;
import com.sprint.part2.sb1hrbankteam03.service.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController implements BackupApi {

  private final BackupService backupService;
  private final IpUtils ipUtils;

  //데이터 백업 목록 전체 조회
  @GetMapping
  public ResponseEntity<CursorPageResponseBackupDto> getBackups(
      @RequestParam(required = false) String worker, @RequestParam(required = false) String status,
      @RequestParam(required = false) String startedAtFrom, // date-time
      @RequestParam(required = false) String startedAtTo, // date-time
      @RequestParam(required = false, defaultValue = "0") long idAfter, //int64
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false, defaultValue = "10") int size, //int32
      @RequestParam(required = false, defaultValue = "startedAt") String sortField,
      @RequestParam(required = false, defaultValue = "DESC") String sortDirection) {

    RequestBackupDto requestBackupDto = new RequestBackupDto(worker, status, startedAtFrom,
        startedAtTo, idAfter, cursor, size, sortField, sortDirection);

    return ResponseEntity.ok(backupService.getBackups(requestBackupDto));
  }

  //데이터 백업 생성
  @PostMapping
  public ResponseEntity<BackupDto> createBackup(HttpServletRequest request) {
    String ipAddress = ipUtils.getClientIp(request);
    return ResponseEntity.ok(backupService.createBackup(ipAddress));
  }

  //최근 백업 정보 조회
  @GetMapping("/latest")
  public BackupDto getLatestBackup(
      @RequestParam(required = false, defaultValue = "COMPLETED") String status) {
    return backupService.getLatestBackup(status);
  }

}
