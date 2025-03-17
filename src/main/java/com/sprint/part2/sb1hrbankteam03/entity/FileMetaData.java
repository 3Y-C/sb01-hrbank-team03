package com.sprint.part2.sb1hrbankteam03.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "file_meta_data")
public class FileMetaData extends BaseEntity{

  @Column(name = "name",nullable = false)
  String fileName;

  @Column(name = "type",nullable = false)
  String fileType;

  @Column(name = "size",nullable = false)
  Long fileSize;

}
