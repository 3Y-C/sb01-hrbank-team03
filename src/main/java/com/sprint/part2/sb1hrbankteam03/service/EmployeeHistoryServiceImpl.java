package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.dto.PageResponse;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.ChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.DiffDto;
import com.sprint.part2.sb1hrbankteam03.entity.ChangeType;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeChangeDetail;
import com.sprint.part2.sb1hrbankteam03.entity.EmployeeHistory;
import com.sprint.part2.sb1hrbankteam03.mapper.EmployeeChangeDetailMapper;
import com.sprint.part2.sb1hrbankteam03.mapper.EmployeeHistoryMapper;
import com.sprint.part2.sb1hrbankteam03.mapper.PageResponseMapper;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeChangeDetailRepository;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeHistoryRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class EmployeeHistoryServiceImpl implements EmployeeHistoryService {

  private final EmployeeHistoryRepository employeeHistoryRepository;
  private final EmployeeChangeDetailRepository employeeChangeDetailRepository;
  private final EmployeeHistoryMapper employeeHistoryMapper;
  private final PageResponseMapper pageResponseMapper;
  private final EmployeeChangeDetailMapper employeeChangeDetailMapper;

  @Override
  public EmployeeHistory createHistory(String employeeNumber, ChangeType type, String memo) {
    String ipAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
    Instant now = Instant.now();

    // 메모 디폴트 처리
    String finalMemo;
    if (memo == null || memo.isBlank()) {
      switch (type) {
        case CREATED -> finalMemo = "신규 직원 등록";
        case UPDATED -> finalMemo = "직원 정보 수정";
        case DELETED -> finalMemo = "직원 삭제";
        default -> finalMemo = "";
      }
    } else {
      finalMemo = memo;
    }

    EmployeeHistory history = new EmployeeHistory(employeeNumber, type, finalMemo, ipAddress, now);
    return employeeHistoryRepository.save(history);
  }

  public void saveChangeDetails(EmployeeHistory history, List<EmployeeChangeDetail> details) {
    for (EmployeeChangeDetail d : details) {
      d.setEmployeeHistory(history);
    }
    employeeChangeDetailRepository.saveAll(details);
  }


  @Transactional(readOnly = true)
  public PageResponse<ChangeLogDto> getChangeLogs(
      String employeeNumber, String memo, String ipAddress, ChangeType changeType,
      Instant atFrom, Instant atTo, Instant cursor, String sortField,
      String sortDirection, Pageable pageable) {

    // 동적 쿼리 처리: 조건에 맞는 데이터 필터링
    Slice<EmployeeHistory> slice = employeeHistoryRepository.findAllWithFilters(
        employeeNumber, memo, ipAddress, changeType, atFrom, atTo, cursor, pageable
    );

    // 커서 처리
    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1).getAt();
    }

    Slice<ChangeLogDto> dtoSlice = slice.map(employeeHistoryMapper::toDto);
    return pageResponseMapper.fromSlice(dtoSlice, nextCursor);
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