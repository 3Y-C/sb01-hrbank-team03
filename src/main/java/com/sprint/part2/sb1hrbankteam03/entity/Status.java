package com.sprint.part2.sb1hrbankteam03.entity;

public enum Status {
  ACTIVE("ACTIVE"),
  ON_LEAVE("ON_LEAVE"),
  RESIGNED("RESIGNED");

  private final String value;

  Status(String value) {
    this.value = value;
  }

  public static Status from(String value) {
    for (Status status : Status.values()) {
      if (status.value.equalsIgnoreCase(value)) {
        return status;
      }
    }
    return ACTIVE;
  }
}
