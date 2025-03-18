package com.sprint.part2.sb1hrbankteam03.stroage;

import java.io.InputStream;

public interface FileStorage {

  Long put(Long fileMetadataId, byte[] bytes);

  InputStream get(Long fileMetadataId);

  void delete(Long fileMetadataId);
}
