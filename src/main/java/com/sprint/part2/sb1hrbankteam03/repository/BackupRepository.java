package com.sprint.part2.sb1hrbankteam03.repository;

import com.sprint.part2.sb1hrbankteam03.entity.Backup;
import com.sprint.part2.sb1hrbankteam03.entity.enums.BackupStatus;
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
  @Query("SELECT b FROM Backup b " +
      "WHERE ((b.status = :status ) OR ( cast(:status as string) IS NULL))" +
      "AND (:workerIp = '' OR b.workerIp LIKE CONCAT( '%', :workerIp ,'%')) " +
      "AND (b.startAt >= :startAtFrom) " +
      "AND (b.startAt <= :startAtTo) " +
      "AND (:cursorId IS NULL OR " +
      "   (b.startAt IS NULL AND cast( :cursorStartAt as localdatetime ) IS NOT NULL) OR " +
      "   (b.startAt IS NOT NULL AND cast( :cursorStartAt as localdatetime )  IS NULL) OR " +
      "   (b.startAt IS NOT NULL AND cast( :cursorStartAt as localdatetime )  IS NOT NULL AND b.startAt > :cursorStartAt) OR " +
      "   (b.startAt IS NOT NULL AND cast( :cursorStartAt as localdatetime )  IS NOT NULL AND b.startAt = :cursorStartAt AND b.id > :cursorId)) " +
      "ORDER BY CASE WHEN b.startAt IS NULL THEN 1 ELSE 0 END, b.startAt ASC, b.id ASC ")
  List<Backup> findByConditionsOrderByStartAtAsc(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo,
      @Param("cursorStartAt") LocalDateTime cursorStartAt,
      @Param("cursorId") Long cursorId,
      Pageable pageable);

  //조건 일치하는 백업 이력 조회 - 시작 시간 내림차순
  @Query("SELECT b FROM Backup b " +
      "WHERE ((b.status = :status ) OR ( cast(:status as string) IS NULL))" +
      "AND (:workerIp = '' OR b.workerIp LIKE CONCAT( '%', :workerIp ,'%')) " +
      "AND (b.startAt >= :startAtFrom) " +
      "AND (b.startAt <= :startAtTo) " +
      "AND (:cursorId IS NULL OR " +
      "   (b.startAt IS NULL AND cast( :cursorStartAt as localdatetime ) IS NOT NULL) OR " +
      "   (b.startAt IS NOT NULL AND cast( :cursorStartAt as localdatetime )  IS NULL) OR " +
      "   (b.startAt IS NOT NULL AND cast( :cursorStartAt as localdatetime )  IS NOT NULL AND b.startAt < :cursorStartAt) OR " +
      "   (b.startAt IS NOT NULL AND cast( :cursorStartAt as localdatetime )  IS NOT NULL AND b.startAt = :cursorStartAt AND b.id < :cursorId)) " +
      "ORDER BY CASE WHEN b.startAt IS NULL THEN 1 ELSE 0 END, b.startAt DESC, b.id DESC ")
  List<Backup> findByConditionsOrderByStartAtDesc(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo,
      @Param("cursorStartAt") LocalDateTime cursorStartAt,
      @Param("cursorId") Long cursorId,
      Pageable pageable);


  //조건 일치하는 백업 이력 조회 - 종료 시간 오름차순
  @Query("SELECT b FROM Backup b " +
      "WHERE ((b.status = :status ) OR ( cast(:status as string) IS NULL))" +
      "AND (:workerIp = '' OR b.workerIp LIKE CONCAT( '%', :workerIp ,'%')) " +
      "AND (b.startAt >= :startAtFrom) " +
      "AND (b.startAt <= :startAtTo) " +
      "AND (:cursorId IS NULL OR " +
      "   (b.endAt IS NULL AND cast( :cursorStartAt as localdatetime ) IS NOT NULL) OR " +
      "   (b.endAt IS NOT NULL AND cast( :cursorStartAt as localdatetime ) IS NULL) OR " +
      "   (b.endAt IS NOT NULL AND cast( :cursorStartAt as localdatetime ) IS NOT NULL AND b.endAt > :cursorStartAt) OR " +
      "   (b.endAt IS NOT NULL AND cast( :cursorStartAt as localdatetime ) IS NOT NULL AND b.endAt > :cursorStartAt AND b.id > :cursorId)) " +
      "ORDER BY CASE WHEN b.endAt IS NULL THEN 1 ELSE 0 END, b.endAt ASC, b.id ASC ")
  List<Backup> findByConditionsOrderByEndAtAsc(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo,
      @Param("cursorStartAt") LocalDateTime cursorStartAt,
      @Param("cursorId") Long cursorId,
      Pageable pageable);


  //조건 일치하는 백업 이력 조회 - 종료 시간 내림차순
  @Query("SELECT b FROM Backup b "
      + "WHERE ((b.status = :status ) OR ( cast(:status as string) IS NULL))" +
      "AND (:workerIp = '' OR b.workerIp LIKE CONCAT( '%', :workerIp ,'%')) " +
      "AND (b.startAt >= :startAtFrom) " +
      "AND (b.startAt <= :startAtTo) " +
      "AND (:cursorId IS NULL OR" +
      "   (b.endAt IS NULL AND cast( :cursorStartAt as localdatetime ) IS NOT NULL) OR " +
      "   (b.endAt IS NOT NULL AND cast( :cursorStartAt as localdatetime ) IS NULL) OR " +
      "   (b.endAt IS NOT NULL AND cast( :cursorStartAt as localdatetime ) IS NOT NULL AND b.endAt < :cursorStartAt) OR " +
      "   (b.endAt IS NOT NULL AND cast( :cursorStartAt as localdatetime ) IS NOT NULL AND b.endAt < :cursorStartAt AND b.id < :cursorId)) " +
      "ORDER BY CASE WHEN b.endAt IS NULL THEN 1 ELSE 0 END, b.endAt DESC, b.id DESC ")
  List<Backup> findByConditionsOrderByEndAtDesc(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo,
      @Param("cursorStartAt") LocalDateTime cursorStartAt,
      @Param("cursorId") Long cursorId,
      Pageable pageable);


  // 다음 페이지 존재 여부를 확인하기 위한 1개 더 조회하는 메서드
  @Query("SELECT COUNT(b) > 0 FROM Backup b " + "WHERE b.id < :id ")
  boolean existsByIdAfter(@Param("id") Long id);


  // 조건 결과 개수
  @Query(value = "SELECT COUNT(b) FROM Backup b " +
      "WHERE ((b.status = :status ) OR ( cast(:status as string) IS NULL))" +
      "AND (:workerIp = '' OR b.workerIp LIKE CONCAT( '%', :workerIp ,'%')) " +
      "AND (b.startAt >= :startAtFrom) " +
      "AND (b.startAt <= :startAtTo)")
  long countByConditions(
      @Param("status") BackupStatus status,
      @Param("workerIp") String workerIp,
      @Param("startAtFrom") LocalDateTime startAtFrom,
      @Param("startAtTo") LocalDateTime startAtTo
  );

  long count();
}
