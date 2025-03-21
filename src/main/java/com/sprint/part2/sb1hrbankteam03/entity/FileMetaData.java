package com.sprint.part2.sb1hrbankteam03.entity;

import com.sprint.part2.sb1hrbankteam03.entity.base.BaseEntity;
import com.sprint.part2.sb1hrbankteam03.entity.enums.FileCategory;
import jakarta.persistence.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "file_meta_data")
public class FileMetaData extends BaseEntity {

  @Column(name = "name",nullable = false)
  String fileName;

  @Column(name = "type",nullable = false)
  String fileType;

  @Column(name = "size",nullable = false)
  Long fileSize;

  @Enumerated(EnumType.STRING)
  @Column(name = "file_category",nullable = false, columnDefinition = "file_category_enum")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private FileCategory fileCategory;

}
