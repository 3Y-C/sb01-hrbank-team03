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

  //조건 일치하는 백업 이력 조회 - 시작 시간 오름차순
  @Query("SELECT b FROM Backup b "
      + "WHERE (:status IS NULL OR b.status = :status) " +
      "AND (:workerIp IS NULL OR b.workerIp LIKE CONCAT('%', :workerIp, '%')) " +
      "AND (:startAtFrom IS NULL OR b.startAt >= :startAtFrom) " +
      "AND (:startAtTo IS NULL OR b.startAt <= :startAtTo) "+
      "AND (:cursorStartAt IS NULL OR b.startAt > :cursorStartAt " +
      "     OR (b.startAt = :cursorStartAt AND b.id > :cursorId))" +
      "ORDER BY b.startAt ASC, b.id ASC")
  List<Backup> findByConditionsOrderByStartAtAsc(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo,
      @Param("cursorStartAt") LocalDateTime cursorStartAt,
      @Param("cursorId") Long cursorId,
      Pageable pageable);


  //조건 일치하는 백업 이력 조회 - 시작 시간 내림차순
  @Query("SELECT b FROM Backup b "
      + "WHERE (:status IS NULL OR b.status = :status) " +
      "AND (:workerIp IS NULL OR b.workerIp LIKE CONCAT('%', :workerIp, '%')) " +
      "AND (:startAtFrom IS NULL OR b.startAt >= :startAtFrom) " +
      "AND (:startAtTo IS NULL OR b.startAt <= :startAtTo)" +
      "AND (:cursorStartAt IS NULL OR b.startAt < :cursorStartAt " +
      "     OR (b.startAt = :cursorStartAt AND b.id < :cursorId))" +
  "ORDER BY b.startAt DESC, b.id DESC")
  List<Backup> findByConditionsOrderByStartAtDesc(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo,
      @Param("cursorStartAt") LocalDateTime cursorStartAt,
      @Param("cursorId") Long cursorId,
      Pageable pageable);


  //조건 일치하는 백업 이력 조회 - 종료 시간 오름차순
  @Query("SELECT b FROM Backup b "
      + "WHERE (:status IS NULL OR b.status = :status) " +
      "AND (:workerIp IS NULL OR b.workerIp LIKE CONCAT('%', :workerIp, '%')) " +
      "AND (:startAtFrom IS NULL OR b.startAt >= :startAtFrom) " +
      "AND (:startAtTo IS NULL OR b.startAt <= :startAtTo)" +
      "AND (:cursorEndAt IS NULL OR b.endAt > :cursorEndAt " +
      "     OR (b.endAt = :cursorEndAt AND b.id > :cursorId))" +
    "ORDER BY b.endAt ASC, b.id ASC")
  List<Backup> findByConditionsOrderByEndAtAsc(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo,
      @Param("cursorEndAt") LocalDateTime cursorEndAt,
      @Param("cursorId") Long cursorId,
      Pageable pageable);


  //조건 일치하는 백업 이력 조회 - 종료 시간 내림차순
  @Query("SELECT b FROM Backup b "
      + "WHERE (:status IS NULL OR b.status = :status) " +
      "AND (:workerIp IS NULL OR b.workerIp LIKE CONCAT('%', :workerIp, '%')) " +
      "AND (:startAtFrom IS NULL OR b.startAt >= :startAtFrom) " +
      "AND (:startAtTo IS NULL OR b.startAt <= :startAtTo)" +
      "AND (:cursorEndAt IS NULL OR b.endAt < :cursorEndAt " +
      "     OR (b.endAt = :cursorEndAt AND b.id < :cursorId))" +
    "ORDER BY b.endAt DESC, b.id DESC")
  List<Backup> findByConditionsOrderByEndAtDesc(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo,
      @Param("cursorEndAt") LocalDateTime cursorEndAt,
      @Param("cursorId") Long cursorId,
      Pageable pageable);


  // 다음 페이지 존재 여부를 확인하기 위한 1개 더 조회하는 메서드
  @Query("SELECT COUNT(b) > 0 FROM Backup b " + "WHERE b.id < :id ")
  boolean existsByIdAfter(@Param("id") Long id);


  // 조건 결과 개수
  @Query("SELECT COUNT(b) FROM Backup b "
      + "WHERE (:status IS NULL OR b.status = :status) " +
      "AND (:workerIp IS NULL OR b.workerIp LIKE CONCAT('%', :workerIp, '%')) " +
      "AND (:startAtFrom IS NULL OR b.startAt >= :startAtFrom) " +
      "AND (:startAtTo IS NULL OR b.startAt <= :startAtTo)")
  long countByConditions(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo
  );

}
