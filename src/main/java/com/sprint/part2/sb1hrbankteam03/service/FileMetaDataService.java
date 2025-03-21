package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.entity.enums.FileCategory;
import com.sprint.part2.sb1hrbankteam03.entity.FileMetaData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileMetaDataService {

  FileMetaData create(MultipartFile multipartFile, FileCategory fileCategory);
  ResponseEntity<?> download(Long fileMetaDataId);
  void delete(Long fileMetadataId);
}
