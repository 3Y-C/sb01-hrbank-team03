package com.sprint.part2.sb1hrbankteam03.service.implement;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.ChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.DiffDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeChangeInfo;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeSnapshotDto;
import com.sprint.part2.sb1hrbankteam03.entity.enums.ChangeType;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeChangeDetail;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import com.sprint.part2.sb1hrbankteam03.mapper.EmployeeChangeDetailMapper;
import com.sprint.part2.sb1hrbankteam03.mapper.EmployeeHistoryMapper;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeChangeDetailRepository;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeHistoryRepository;
import com.sprint.part2.sb1hrbankteam03.service.EmployeeHistoryService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeHistoryServiceImpl implements EmployeeHistoryService {

  private final EmployeeHistoryRepository employeeHistoryRepository;
  private final EmployeeChangeDetailRepository employeeChangeDetailRepository;
  private final EmployeeHistoryMapper employeeHistoryMapper;
  private final EmployeeChangeDetailMapper employeeChangeDetailMapper;

  @Override
  @Transactional
  public void recordHistoryFromSnapshot(EmployeeSnapshotDto before, EmployeeSnapshotDto after, ChangeType type, String memo, String ipAddress) {
    String employeeNumber = (after != null) ? after.getEmployeeNumber() : before.getEmployeeNumber();

    Instant now = Instant.now();
    String finalMemo = (memo == null || memo.isBlank()) ? switch (type) {
      case CREATED -> "신규 직원 등록";
      case UPDATED -> "직원 정보 수정";
      case DELETED -> "직원 삭제";
      default -> "";
    } : memo;

    EmployeeHistory history = new EmployeeHistory(employeeNumber, type, finalMemo, ipAddress, now);
    employeeHistoryRepository.save(history);

    List<EmployeeChangeInfo> infos = new ArrayList<>();

    if (type == ChangeType.CREATED && after != null) {
      infos.add(new EmployeeChangeInfo("name", null, after.getName()));
      infos.add(new EmployeeChangeInfo("email", null, after.getEmail()));
      infos.add(new EmployeeChangeInfo("department", null, after.getDepartment()));
      infos.add(new EmployeeChangeInfo("position", null, after.getPosition()));
      infos.add(new EmployeeChangeInfo("hireDate", null, after.getHireDate()));
      infos.add(new EmployeeChangeInfo("status", null, after.getStatus()));
    } else if (type == ChangeType.DELETED && before != null) {
      infos.add(new EmployeeChangeInfo("name", before.getName(), null));
      infos.add(new EmployeeChangeInfo("email", before.getEmail(), null));
      infos.add(new EmployeeChangeInfo("department", before.getDepartment(), null));
      infos.add(new EmployeeChangeInfo("position", before.getPosition(), null));
      infos.add(new EmployeeChangeInfo("hireDate", before.getHireDate(), null));
      infos.add(new EmployeeChangeInfo("status", before.getStatus(), null));
    } else if (type == ChangeType.UPDATED && before != null && after != null) {
      if (!Objects.equals(before.getName(), after.getName())) {
        infos.add(new EmployeeChangeInfo("name", before.getName(), after.getName()));
      }
      if (!Objects.equals(before.getEmail(), after.getEmail())) {
        infos.add(new EmployeeChangeInfo("email", before.getEmail(), after.getEmail()));
      }
      if (!Objects.equals(before.getDepartment(), after.getDepartment())) {
        infos.add(new EmployeeChangeInfo("department", before.getDepartment(), after.getDepartment()));
      }
      if (!Objects.equals(before.getPosition(), after.getPosition())) {
        infos.add(new EmployeeChangeInfo("position", before.getPosition(), after.getPosition()));
      }
      if (!Objects.equals(before.getHireDate(), after.getHireDate())) {
        infos.add(new EmployeeChangeInfo("hireDate", before.getHireDate(), after.getHireDate()));
      }
      if (!Objects.equals(before.getStatus(), after.getStatus())) {
        infos.add(new EmployeeChangeInfo("status", before.getStatus(), after.getStatus()));
      }
    }
    List<EmployeeChangeDetail> details = infos.stream()
        .map(info -> new EmployeeChangeDetail(history, info.getField(), info.getOldValue(), info.getNewValue()))
        .toList();

    employeeChangeDetailRepository.saveAll(details);
  }


  @Transactional(readOnly = true)
  public CursorPageResponseChangeLogDto getChangeLogs(
      String employeeNumber, String memo, String ipAddress, ChangeType changeType,
      Instant atFrom, Instant atTo, String cursor, Long idAfter, String sortField,
      String sortDirection, Pageable pageable) {

    // 쿼리 실행
    Slice<EmployeeHistory> slice = employeeHistoryRepository.findAllWithFilters(
        employeeNumber, memo, ipAddress, changeType, atFrom, atTo, cursor, idAfter, pageable, sortField, sortDirection
    );

    // DTO 변환
    Slice<ChangeLogDto> dtoSlice = slice.map(employeeHistoryMapper::toDto);

    // 커서 계산 (정렬 필드에 따라 동적 처리)
    String nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      EmployeeHistory last = slice.getContent().get(slice.getContent().size() - 1);
      if ("ipAddress".equals(sortField)) {
        nextCursor = last.getIpAddress();
      } else {
        nextCursor = last.getAt().toString();  // 기본: at
      }
    }

    Long totalElements = employeeHistoryRepository.countByFilters(employeeNumber, memo, ipAddress, changeType, atFrom, atTo);

    return employeeHistoryMapper.fromSlice(dtoSlice, nextCursor, totalElements);
  }

  @Override
  public List<DiffDto> getChangeDetails(Long historyId) {
    // 특정 historyId의 변경 상세 내용 조회
    EmployeeHistory employeeHistory = employeeHistoryRepository.findById(historyId)
        .orElseThrow(() -> new RuntimeException("History not found"));

    // 이력의 변경 상세 내용을 찾고, 이를 DiffDto로 변환
    List<EmployeeChangeDetail> changeDetails = employeeChangeDetailRepository.findByEmployeeHistoryId(historyId);

    // EmployeeChangeDetail을 DiffDto로 매핑
    return changeDetails.stream()
        .map(employeeChangeDetailMapper::toDto)
        .toList();
  }

  @Override
  public long getChangeLogCount(Instant fromDate, Instant toDate) {
    return employeeHistoryRepository.countByAtBetween(fromDate, toDate);
  }

}