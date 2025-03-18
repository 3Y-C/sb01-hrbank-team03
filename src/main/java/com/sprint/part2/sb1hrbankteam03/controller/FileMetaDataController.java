package com.sprint.part2.sb1hrbankteam03.controller;

import com.sprint.part2.sb1hrbankteam03.service.FileMetaDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileMetaDataController {

  private final FileMetaDataService fileMetaDataService;

  @GetMapping("{fileMetaDataId}/download")
  public ResponseEntity<?> download(@PathVariable("fileMetaDataId") Long fileMetaDataId){
    return fileMetaDataService.download(fileMetaDataId);
  }
}
