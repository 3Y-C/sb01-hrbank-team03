package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.entity.Backup;
import com.sprint.part2.sb1hrbankteam03.entity.BackupStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BackupRepository extends JpaRepository<Backup, Long> {

  //데이터 백업 이력 중 가장 최신 작업 조회
  Optional<Backup> findTopByStatusOrderByEndAtDesc(BackupStatus status);

  //조건 일치하는 백업 이력 조회 내림차순
  @Query("SELECT b FROM Backup b WHERE " +
      "(:status IS NULL OR b.status = :status) AND " +
      "(:workerIp IS NULL OR b.workerIp LIKE CONCAT('%', :workerIp, '%')) AND " +
      "(:startAtFrom IS NULL OR b.startAt >= :startAtFrom) AND " +
      "(:startAtTo IS NULL OR b.startAt <= :startAtTo) AND " +
      "(:cursor IS NULL OR b.id < :cursor)" + // cursor를 ID로 비교
      "ORDER BY b.id DESC")
  List<Backup> findByConditions(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo,
      @Param("cursor") Long cursor,
      Pageable pageable);

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
