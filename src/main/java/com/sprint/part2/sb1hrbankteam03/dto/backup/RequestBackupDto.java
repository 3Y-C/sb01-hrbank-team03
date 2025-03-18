package com.sprint.part2.sb1hrbankteam03.dto.backup;

import lombok.Getter;

@Getter
public class RequestBackupDto {

  private String worker;
  private String status;
  private String startedAtFrom;
  private String startedAtTo;
  private int idAfter;
  private String cursor;
  private int size;
  private String sortField;
  private String sortDirection;

  public RequestBackupDto(String worker, String status, String startedAtFrom, String startedAtTo,
      int idAfter, String cursor, int size, String sortField, String sortDirection) {
    this.worker = worker;
    this.status = status;
    this.startedAtFrom = startedAtFrom;
    this.startedAtTo = startedAtTo;
    this.idAfter = idAfter;
    this.cursor = cursor;
    this.size = size;
    this.sortField = sortField;
    this.sortDirection = sortDirection;
  }
}
