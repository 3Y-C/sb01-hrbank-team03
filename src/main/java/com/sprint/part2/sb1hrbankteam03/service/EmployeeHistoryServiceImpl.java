package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.ChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.DiffDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeChangeInfo;
import com.sprint.part2.sb1hrbankteam03.entity.ChangeType;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeChangeDetail;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import com.sprint.part2.sb1hrbankteam03.mapper.EmployeeChangeDetailMapper;
import com.sprint.part2.sb1hrbankteam03.mapper.EmployeeHistoryMapper;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeChangeDetailRepository;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeHistoryRepository;
import java.time.Instant;
import java.util.List;
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
  public EmployeeHistory createHistoryWithDetails(String employeeNumber, ChangeType type, String memo, List<EmployeeChangeInfo> infos, String ipAddress) {
    Instant now = Instant.now();

    String finalMemo = (memo == null || memo.isBlank()) ? switch (type) {
      case CREATED -> "신규 직원 등록";
      case UPDATED -> "직원 정보 수정";
      case DELETED -> "직원 삭제";
      default -> "";
    } : memo;

    EmployeeHistory history = new EmployeeHistory(employeeNumber, type, finalMemo, ipAddress, now);
    employeeHistoryRepository.save(history);

    List<EmployeeChangeDetail> details = infos.stream()
        .map(info -> new EmployeeChangeDetail(history, info.getField(), info.getOldValue(), info.getNewValue()))
        .toList();

    employeeChangeDetailRepository.saveAll(details);
    return history;
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