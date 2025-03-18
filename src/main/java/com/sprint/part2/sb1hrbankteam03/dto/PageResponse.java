package com.sprint.part2.sb1hrbankteam03.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> {

  private List<T> content;
  private Object nextCursor;
  private int size;
  private boolean hasNext;
  private Long totalElements;

}