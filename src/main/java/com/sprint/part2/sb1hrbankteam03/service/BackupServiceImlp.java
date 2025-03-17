package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.BackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.RequestBackupDto;
import com.sprint.part2.sb1hrbankteam03.entity.Backup;
import com.sprint.part2.sb1hrbankteam03.entity.BackupStatus;

import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import com.sprint.part2.sb1hrbankteam03.entity.FileMetaData;
import com.sprint.part2.sb1hrbankteam03.entity.Employee;
import com.sprint.part2.sb1hrbankteam03.mapper.BackupMapper;
import com.sprint.part2.sb1hrbankteam03.repository.BackupRepository;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeHistoryRepository;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeRepository;
import com.sprint.part2.sb1hrbankteam03.repository.FileMetaDataRepository;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackupServiceImlp implements BackupService {

  private final BackupRepository backupRepository;
  private final EmployeeHistoryRepository employeeHistoryRepository;
  private final BackupMapper backupMapper;
  private final EmployeeRepository employeeRepository;
  private final FileMetaDataRepository fileMetaDataRepository;
  private final FileStorage fileStorage;

  @Override
  public BackupDto createBackup(String workerIp) {

    //가장 최근 성공한 백업 이력을 가져온다
    Backup latestBackup = backupRepository.findTopByStatusOrderByEndAtDesc(BackupStatus.COMPLETED)
        .orElse(null);

    if (latestBackup == null) {
      //가장 최근 백업 이력이 없다면, 새로 생성
      latestBackup = createBackupFile(workerIp);
      backupRepository.save(latestBackup);
      return backupMapper.toDto(latestBackup);
    }

    //가져왔다면 가장 최근 변경 이력을 가져온다.
    List<EmployeeHistory> byEditedHistoryAtAfter = employeeHistoryRepository
        .findByEditedAtAfterOrderByEditedAtDesc(latestBackup.getEndAt().toInstant(ZoneOffset.UTC));
    //todo- zone offset 논의필요?

    //최근 변경 이력이 마지막 백업(건너뛰지않은)시간보다 나중이라면 변경이 생긴 것이므로, 백업 파일을 만들어야한다.
    //사이즈가 0이면, 배치 작업 이후 최근 변경 이력이 없는 것이므로 "건너뜀"으로 생성후, 결과 반환
    if (byEditedHistoryAtAfter.isEmpty()) {
      Backup backup = new Backup(
          workerIp,
          BackupStatus.SKIPPED,
          LocalDateTime.now(),
          LocalDateTime.now(),
          null
      );
      backupRepository.save(backup);
      return backupMapper.toDto(backup);
    } else {
      //있다면 새로 생성
      Backup newBackup = backupRepository.save(createBackupFile(workerIp));
      return backupMapper.toDto(newBackup);
    }
  }

  @Override
  public List<BackupDto> getBackups(RequestBackupDto requestBackupDto) {
    if (requestBackupDto == null) {
      throw new IllegalArgumentException("requestBackupDto cannot be null");
    }

    LocalDateTime startedAtFrom = null;
    if (requestBackupDto.getStartedAtFrom() != null) {
      startedAtFrom = LocalDateTime.parse(requestBackupDto.getStartedAtFrom());
    }

    LocalDateTime startedAtTo = null;
    if (requestBackupDto.getStartedAtTo() != null) {
      startedAtTo = LocalDateTime.parse(requestBackupDto.getStartedAtTo());
    }

    return backupRepository.findByConditions(
        BackupStatus.valueOf(requestBackupDto.getStatus()),
        requestBackupDto.getWorker(),
        startedAtFrom,
        startedAtTo
    ).stream().map(backupMapper::toDto).toList();
  }

  @Override
  public BackupDto getLatestBackup(String status) {
    BackupStatus backupStatus = BackupStatus.valueOf(status);

    Backup latestBackup = backupRepository.findTopByStatusOrderByEndAtDesc(backupStatus)
        .orElse(null);
    if (latestBackup == null) {
      return null;
    }
    return backupMapper.toDto(latestBackup);
  }

  private Backup createBackupFile(String workerIp) {

    Backup backup = new Backup(
        workerIp,
        BackupStatus.IN_PROGRESS,
        LocalDateTime.now(),
        null,
        null
    );

    backup = backupRepository.save(backup);

    try {
      String fileName =
          "employee_backup_" + LocalDateTime.now().toString().replace(":", "-") + ".csv";
      String fileType = "text/csv";

      //임시 파일 생성
      Path tempFile = Files.createTempFile(fileName, ".csv");

      FileMetaData backupFile = new FileMetaData(fileName, fileType, tempFile.toFile().length());

      //버퍼 writer 로 백업 데이터 쓰기
      try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

        //todo - 페이지네이션으로 배치 작업

        //모든 직원 가져와서 작성
        //todo - 변경 안 된 건 그전 파일에서 그대로 가져오고, 변경된 부분만 수정하도록 할 수 있을까?
        List<Employee> allEmployees = employeeRepository.findAll();
        for (Employee employee : allEmployees) {
          writer.write(String.format("%d,%s,%s,%s,%s,%s,%s\n",
              employee.getId(),
              employee.getName(),
              employee.getEmail(),
              employee.getDepartment(),
              employee.getPosition(),
              employee.getHireDate(),
              employee.getStatus()
          ));
        }
      }
      try (InputStream inputStream = Files.newInputStream(tempFile)) {
        fileStorage.put(backupFile.getId(), inputStream.readAllBytes());
      }

      //파일 메타데이터 저장
      FileMetaData savedFile = fileMetaDataRepository.save(backupFile);
      //실제 파일 넣은 후 삭제
      Files.delete(tempFile);

      //완료된 경우 backup 상태 바꾸고 파일 메타 저장
      backup.setStatus(BackupStatus.COMPLETED);
      backup.setEndAt(LocalDateTime.now());
      backup.setBackupFile(savedFile);

    } catch (Exception e) {
      //실패한 경우

      //에러 log 파일 생성
      String fileName = "backup_error_" + LocalDateTime.now().toString().replace(":", "-") + ".log";
      String fileType = "text/plain";

      // 에러 메세지
      String errorMessage = String.format("Backup failed at %s\nError: %s\nStack trace: %s",
          LocalDateTime.now(),
          e.getMessage(),
          Arrays.toString(e.getStackTrace()));

      //메타데이터 생성
      FileMetaData errorLogFile = new FileMetaData(fileName, fileType,
          (long) errorMessage.getBytes().length);

      // 로그 파일 저장
      fileStorage.put(errorLogFile.getId(), errorMessage.getBytes(StandardCharsets.UTF_8));

      // 로그파일 메타데이터 저장
      FileMetaData savedErrorFile = fileMetaDataRepository.save(errorLogFile);

      // 백업 상태 실패로 업데이트
      backup.setStatus(BackupStatus.FAILED);
      backup.setEndAt(LocalDateTime.now());
      backup.setBackupFile(savedErrorFile);
    }
    return backupRepository.save(backup);
  }
}
