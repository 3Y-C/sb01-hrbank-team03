package com.sprint.part2.sb1hrbankteam03.entity;

import com.sprint.part2.sb1hrbankteam03.entity.base.BaseEntity;
import com.sprint.part2.sb1hrbankteam03.entity.enums.BackupStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@Table(name = "backups")
@NoArgsConstructor
public class Backup extends BaseEntity {
  //요청자 ip주소
  private String workerIp;
  // 백업 작업 상태 IN_PROGRESS, COMPLETED, FAILED, SKIPPED
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, name = "status", columnDefinition = "backup_status_enum")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private BackupStatus status;
  // 시작 시간
  private LocalDateTime startAt;
  // 종료 시간
  private LocalDateTime endAt;

  @OneToOne
  @JoinColumn(name = "file_id")
  private FileMetaData backupFile;

  public Backup(String workerIp, BackupStatus status, LocalDateTime startAt, LocalDateTime endAt, FileMetaData backupFile) {
    this.workerIp = workerIp;
    this.status = status;
    this.startAt = startAt;
    this.endAt = endAt;
    this.backupFile = backupFile;
  }

  public void setBackupFile(FileMetaData backupFile) {
    this.backupFile = backupFile;
  }

  public void setStatus(BackupStatus status) {
    this.status = status;
  }

  public void setEndAt(LocalDateTime endAt) {
    this.endAt = endAt;
  }

}
