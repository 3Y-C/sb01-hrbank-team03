package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.entity.Backup;
import com.sprint.part2.sb1hrbankteam03.entity.BackupStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<Backup, Long> {

  //데이터 백업 이력 중 가장 최신 작업 조회
  Optional<Backup> findTopByStatusOrderByEndAtDesc(BackupStatus status);

  List<Backup> findByStatusAndWorkerIpContainingAndStartAtBetweenOrderByStartAtDesc(
      BackupStatus status, String workerIp, LocalDateTime startAtFrom, LocalDateTime startAtTo
      );

/*  @Query(value = "SELECT b FROM Backup b " +
      "WHERE b.status = :status " +
      "and b.startAt between :startedAtFrom and :startedAtTo " +
      "and b.workerIp like :workerIp" +
      " and (:cursor IS NULL OR b.startAt < :cursor)"+ //Default
      "ORDER BY b.startAt DESC") //Default
  List<Backup> findByStatusOrderByEndAtDesc(
      @Param("workerIp") String workerIp,
      @Param("backupStatus") BackupStatus status,
      @Param("startedAtFrom") LocalDateTime startAtFrom,
      @Param("startedAtTo") LocalDateTime startAtTo,
      Pageable pageable);*/

}
