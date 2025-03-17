package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.BackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.RequestBackupDto;

import java.util.List;

public interface BackupService {

  //데이터 백업 생성(등록)
  BackupDto createBackup(String workerIp);

  //데이터 백업 이력 조회
   List<BackupDto> getBackups(RequestBackupDto requestBackupDto);

  //마지막 백업 시간 조회
  BackupDto getLatestBackup(String status);

}
