package com.sprint.part2.sb1hrbankteam03.service.implement;

import com.sprint.part2.sb1hrbankteam03.dto.backup.BackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.backup.CursorPageResponseBackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.backup.ParsedBackupDto;
import com.sprint.part2.sb1hrbankteam03.dto.backup.RequestBackupDto;
import com.sprint.part2.sb1hrbankteam03.entity.Backup;
import com.sprint.part2.sb1hrbankteam03.entity.enums.BackupStatus;

import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import com.sprint.part2.sb1hrbankteam03.entity.enums.FileCategory;
import com.sprint.part2.sb1hrbankteam03.entity.FileMetaData;
import com.sprint.part2.sb1hrbankteam03.entity.Employee;
import com.sprint.part2.sb1hrbankteam03.mapper.BackupMapper;
import com.sprint.part2.sb1hrbankteam03.repository.BackupRepository;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeHistoryRepository;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeRepository;
import com.sprint.part2.sb1hrbankteam03.repository.FileMetaDataRepository;
import com.sprint.part2.sb1hrbankteam03.service.BackupService;
import com.sprint.part2.sb1hrbankteam03.stroage.FileStorage;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

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
        .findByAtAfterOrderByAtDesc(latestBackup.getCreatedAt());

    //최근 변경 이력이 마지막 백업(건너뛰지않은)시간보다 나중이라면 변경이 생긴 것이므로, 백업 파일을 만들어야한다.
    //사이즈가 0이면, 배치 작업 이후 최근 변경 이력이 없는 것이므로 "건너뜀"으로 생성후, 결과 반환
    if (byEditedHistoryAtAfter.isEmpty()) {
      Backup backup = new Backup(
          workerIp,
          BackupStatus.SKIPPED,
          LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul")),
          LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul")),
          null
      );
      backupRepository.save(backup);
      return backupMapper.toDto(backup);
    } else {
      //있다면 새로 생성
      Backup newBackup = createBackupFile(workerIp);
      backupRepository.save(newBackup);
      return backupMapper.toDto(newBackup);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseBackupDto getBackups(RequestBackupDto requestBackupDto) {
    if (requestBackupDto == null) {
      throw new IllegalArgumentException("requestBackupDto cannot be null");
    }

    ParsedBackupDto parsedBackupDto = getParsedRequestBackupDto(requestBackupDto);

    List<Backup> backups; //결과를 담을 리스트
    backups = sortBackups(parsedBackupDto, requestBackupDto.getSortField(),
        requestBackupDto.getSortDirection());

    int requestedSize = parsedBackupDto.getPageSize();
    //다음 페이지 존재하는지 확인하기
    boolean hasNext = backups.size() > requestedSize;

    // 요청한 size보다 많은 결과가 있으면 마지막 항목을 제거해야함!
    if (hasNext) {
      backups = backups.subList(0, requestedSize);
    }

    // 결과가 없거나 다음 페이지가 없는 경우,
    LocalDateTime nextCursor = null;
    Long nextIdAfter = null;
    if (!backups.isEmpty()) {
      // 마지막 항목의 ID를 다음 커서로 설정하기
      if(requestBackupDto.getSortField().equalsIgnoreCase("startedAt")){
        nextCursor = backups.get(backups.size() - 1).getStartAt();
      }else{
        nextCursor = backups.get(backups.size() - 1).getEndAt();
      }
      nextIdAfter = backups.get(backups.size() -1).getId();
    }

    List<BackupDto> backupDtos = backups.stream()
        .map(backupMapper::toDto)
        .toList();

    int actualSize = backupDtos.size();

    return backupMapper.toPageDto(
        backupDtos,
        nextCursor == null ? null : nextCursor.toString(),
        nextIdAfter,
        actualSize,
        parsedBackupDto.getTotalElements(),
        hasNext);
  }

  @Override
  @Transactional(readOnly = true)
  public BackupDto getLatestBackup(String status) {

    BackupStatus backupStatus = BackupStatus.COMPLETED;
    if (status != null) {
      backupStatus = BackupStatus.valueOf(status);
    }

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
        LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul")),
        null,
        null
    );

    backup = backupRepository.save(backup);

    try {
      String fileName =
          "employee_backup_" + LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul"))
.toString().replace(":", "-") + ".csv";
      String fileType = "text/csv";

      //임시 파일 생성
      Path tempFile = Files.createTempFile(fileName, ".csv");

      FileMetaData backupFile = new FileMetaData(fileName, fileType, tempFile.toFile().length(),
          FileCategory.DOCUMENT);

      //버퍼 writer 로 백업 데이터 쓰기
      try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

        int pageSize = 100;
        int pageNumber = 0;
        boolean hasNext = false;

        List<Employee> employees;

        do {
          //모든 직원 페이지네이션으로 가져와서 작성
          Pageable pageable = PageRequest.of(pageNumber, pageSize + 1);
          employees = employeeRepository.findAll(pageable).getContent();

          //다음 페이지 존재하는지 확인하기
          hasNext = employees.size() > pageSize;

          // 요청한 size보다 많은 결과가 있으면 마지막 항목을 제거해야함!
          if (hasNext) {
            employees = employees.subList(0, pageSize);
          }

          writer.write("ID,직원번호,이름,이메일,부서,직급,입사일,상태\n");

          for (Employee employee : employees) {
            writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s\n",
                employee.getId(),
                employee.getEmployeeNumber(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment().getName(),
                employee.getPosition(),
                employee.getHireDate(),
                employee.getStatus()
            ));
          }

          pageNumber++;

        } while (hasNext);
      }
      try (InputStream inputStream = Files.newInputStream(tempFile)) {

        byte[] fileContent = inputStream.readAllBytes();

        backupFile.setFileSize((long) fileContent.length);
        //파일 메타데이터 저장
        FileMetaData savedFile = fileMetaDataRepository.save(backupFile);

        //실제 파일 저장
        fileStorage.put(savedFile.getId(), fileContent);

        //실제 파일 넣은 후 삭제
        Files.delete(tempFile);

        //완료된 경우 backup 상태 바꾸고 파일 메타 저장
        backup.setStatus(BackupStatus.COMPLETED);
        backup.setEndAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul")));
        backup.setBackupFile(savedFile);
        Backup save = backupRepository.save(backup);
        return save;
      }
    } catch (Exception e) {
      handleBackupFailure(e, backup);
    }
    return backupRepository.save(backup);
  }

  //실패한 경우
  private void handleBackupFailure(Exception e, Backup backup) {
    try {
      //에러 log 파일 생성
      String fileName = "backup_error_" + LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul"))
.toString().replace(":", "-") + ".log";
      String fileType = "text/plain";

      // 에러 메세지
      String errorMessage = String.format("Backup failed at %s\nError: %s\nStack trace: %s",
          LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul")),
          e.getMessage(),
          Arrays.toString(e.getStackTrace()));

      //메타데이터 생성
      FileMetaData errorLogFile = new FileMetaData(fileName, fileType,
          (long) errorMessage.getBytes().length, FileCategory.DOCUMENT);
      // 로그파일 메타데이터 저장
      FileMetaData savedErrorFile = fileMetaDataRepository.save(errorLogFile);

      // 로그 파일 저장
      fileStorage.put(savedErrorFile.getId(), errorMessage.getBytes(StandardCharsets.UTF_8));

      // 백업 상태 실패로 업데이트
      backup.setStatus(BackupStatus.FAILED);
      backup.setEndAt(LocalDateTime.now());
      backup.setBackupFile(savedErrorFile);
      backupRepository.save(backup);

    } catch (Exception ex) {
      System.out.println(
          "[Backup Error] 백업 파일 생성 실패: " + ex.getMessage() + "\n" + Arrays.toString(
              ex.getStackTrace()));
    }
  }

  private ParsedBackupDto getParsedRequestBackupDto(RequestBackupDto requestBackupDto) {

    String workerIp = requestBackupDto.getWorker() == null ? "" : requestBackupDto.getWorker();
    BackupStatus backupStatus = null;
    if (requestBackupDto.getStatus() != null && !requestBackupDto.getStatus().isEmpty()) {
      backupStatus = BackupStatus.valueOf(requestBackupDto.getStatus());
    }

    //startedAtFrom 가 Null 이거나 파싱 불가능한 값이라면 LocalDateTime 최소값으로 설정
    LocalDateTime startedAtFrom = LocalDateTime.of(1970, 1, 1, 0, 0, 0);;
    if (requestBackupDto.getStartedAtFrom() != null) {
      startedAtFrom = LocalDateTime.ofInstant(Instant.parse(requestBackupDto.getStartedAtFrom()), ZoneId.of("Asia/Seoul"));
    }
    //startedAtTo 가 Null 이거나 파싱 불가능한 값이라면 LocalDateTime 최대값으로 설정
    LocalDateTime startedAtTo = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
    if (requestBackupDto.getStartedAtTo() != null ) {
      startedAtTo = LocalDateTime.ofInstant(Instant.parse(requestBackupDto.getStartedAtTo()),ZoneId.of("Asia/Seoul"));
    }

    int size = requestBackupDto.getSize() == 10 ? 10 : requestBackupDto.getSize();
    PageRequest pageRequest = PageRequest.of(0, size + 1);

    // 커서 값 설정
    Long cursorId = requestBackupDto.getIdAfter();
    LocalDateTime cursorAt = null;

    if (requestBackupDto.getCursor() != null && !requestBackupDto.getCursor().isEmpty()) {
      cursorAt = LocalDateTime.parse(requestBackupDto.getCursor());
    }

    //조회 결과 총 개수
    long totalElements = backupRepository.countByConditions(
        backupStatus,
        workerIp,
        startedAtFrom,
        startedAtTo
    );

    return new ParsedBackupDto(
        workerIp,
        backupStatus,
        startedAtFrom,
        startedAtTo,
        size,
        cursorId,
        cursorAt,
        pageRequest,
        totalElements
    );
  }

  // 정렬 기준(필드), 방향(오름차순/내림차순)에 따라 결과를 반환하는 함수
  private List<Backup> sortBackups(ParsedBackupDto parsedBackupDto, String sortField,
      String sortDirection) {

    List<Backup> backups = new ArrayList<>();

    if (sortDirection.equalsIgnoreCase("asc")) {
      if (sortField.equalsIgnoreCase("endedAt")) {
        backups = backupRepository.findByConditionsOrderByEndAtAsc(
            parsedBackupDto.getBackupStatus(),
            parsedBackupDto.getWorkerIp(),
            parsedBackupDto.getStartedAtFrom(),
            parsedBackupDto.getStartedAtTo(),
            parsedBackupDto.getCursorStartAt(),
            parsedBackupDto.getCursorId(),
            parsedBackupDto.getPageRequest()
        );
      } else {
        backups = backupRepository.findByConditionsOrderByStartAtAsc(
            parsedBackupDto.getBackupStatus(),
            parsedBackupDto.getWorkerIp(),
            parsedBackupDto.getStartedAtFrom(),
            parsedBackupDto.getStartedAtTo(),
            parsedBackupDto.getCursorStartAt(),
            parsedBackupDto.getCursorId(),
            parsedBackupDto.getPageRequest()
        );
      }

    } else {
      if (sortField.equalsIgnoreCase("endedAt")) {
        backups = backupRepository.findByConditionsOrderByEndAtDesc(
            parsedBackupDto.getBackupStatus(),
            parsedBackupDto.getWorkerIp(),
            parsedBackupDto.getStartedAtFrom(),
            parsedBackupDto.getStartedAtTo(),
            parsedBackupDto.getCursorStartAt(),
            parsedBackupDto.getCursorId(),
            parsedBackupDto.getPageRequest()
        );
      } else {
        backups = backupRepository.findByConditionsOrderByStartAtDesc(
            parsedBackupDto.getBackupStatus(),
            parsedBackupDto.getWorkerIp(),
            parsedBackupDto.getStartedAtFrom(),
            parsedBackupDto.getStartedAtTo(),
            parsedBackupDto.getCursorStartAt(),
            parsedBackupDto.getCursorId(),
            parsedBackupDto.getPageRequest()
        );
      }
    }
    return backups;
  }
}
