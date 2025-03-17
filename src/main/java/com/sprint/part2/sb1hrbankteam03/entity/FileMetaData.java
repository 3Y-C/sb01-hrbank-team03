package com.sprint.part2.sb1hrbankteam03.entity;

import jakarta.persistence.Entity;

@Entity
public class FileMetaData extends BaseEntity{

  String fileName;
  String fileType;
  Long fileSize;

}
