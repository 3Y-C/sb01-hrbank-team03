package com.sprint.part2.sb1hrbankteam03.stroage;

import com.sprint.part2.sb1hrbankteam03.entity.FileMetaData;
import java.io.InputStream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LocalFileStorage implements FileStorage{

  @Override
  public Long put(Long fileMetadataId, byte[] bytes) {
    return null;
  }

  @Override
  public InputStream get(Long fileMetadataId) {
    return null;
  }

  @Override
  public ResponseEntity<?> download(FileMetaData fileMetaData) {
    return null;
  }
}
