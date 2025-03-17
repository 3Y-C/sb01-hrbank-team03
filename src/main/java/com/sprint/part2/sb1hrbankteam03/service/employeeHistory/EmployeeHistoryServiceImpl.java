package com.sprint.part2.sb1hrbankteam03.service.employeeHistory;

import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeChangeDetailDto;
import com.sprint.part2.sb1hrbankteam03.dto.employeeHistory.EmployeeHistoryDto;
import com.sprint.part2.sb1hrbankteam03.entity.ChangeType;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeChangeDetailRepository;
import com.sprint.part2.sb1hrbankteam03.repository.EmployeeHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeHistoryServiceImpl implements EmployeeHistoryService {

  private final EmployeeHistoryRepository employeeHistoryRepository;
  private final EmployeeChangeDetailRepository employeeChangeDetailRepository;

  // 이력 목록 조회
  public Page<EmployeeHistoryDto> getChangeLogs(String employeeNumber, String memo, String ipAddress,
      ChangeType changeType, LocalDateTime startTime, LocalDateTime endTime,
      int page, int size, String sortBy, String sortDirection) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

    return employeeHistoryRepository.findChangeLogs(
        employeeNumber, memo, ipAddress, changeType, startTime, endTime, pageable);
  }

  // 특정 이력의 변경 상세 내역 조회
  public List<EmployeeChangeDetailDto> getChangeDetails(Long historyId) {
    return employeeChangeDetailRepository.findByEmployeeHistoryId(historyId)
        .stream()
        .map(EmployeeChangeDetailDto::fromEntity)
        .toList();
  }

  public long getChangeLogCount(LocalDateTime fromDate, LocalDateTime toDate) {
    return employeeHistoryRepository.countByEditedAtBetween(fromDate, toDate);
  }
}