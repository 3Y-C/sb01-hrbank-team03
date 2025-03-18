package com.sprint.part2.sb1hrbankteam03.controller;

import com.sprint.part2.sb1hrbankteam03.dto.backup.BackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.backup.CursorPageResponseBackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.backup.RequestBackupDto;
import com.sprint.part2.sb1hrbankteam03.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController {

  private final BackupService backupService;

  //데이터 백업 목록 전체 조회
  @GetMapping
  public CursorPageResponseBackupDto getBackups(@RequestParam(required = false) String worker,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String startedAtFrom, // date-time
      @RequestParam(required = false) String startedAtTo, // date-time
      @RequestParam(required = false) int idAfter, //int64
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false, defaultValue = "10") int size, //int32
      @RequestParam(required = false, defaultValue = "startedAt") String sortField,
      @RequestParam(required = false, defaultValue = "DESC") String sortDirection){

    RequestBackupDto requestBackupDto = new RequestBackupDto(worker,status, startedAtFrom, startedAtTo, idAfter, cursor, size, sortField, sortDirection);

    return backupService.getBackups(requestBackupDto);
  }

  //데이터 백업 생성
  @PostMapping
  public BackupDto createBackup() {
    //todo - 요청자 ip 읽어오기
    String workerIp= null;
    return backupService.createBackup(workerIp);
  }

  //최근 백업 정보 조회
  @GetMapping("/api/backups/latest")
  public BackupDto getLatestBackup(@RequestParam(required = false, defaultValue = "COMPLETED") String status){
    return backupService.getLatestBackup(status);
  }

}
