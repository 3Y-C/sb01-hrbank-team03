package com.sprint.part2.sb1hrbankteam03.stroage;

import com.sprint.part2.sb1hrbankteam03.entity.FileMetaData;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface FileStorage {

  Long put(Long fileMetadataId, byte[] bytes);

  InputStream get(Long fileMetadataId);

  ResponseEntity<?> download(FileMetaData fileMetaData);
}
