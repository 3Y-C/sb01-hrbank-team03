package com.sprint.part2.sb1hrbankteam03.stroage;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(name = "hrbank.storage.type", havingValue = "local")
public class LocalFileStorage implements FileStorage{

  private final Path root;

  public LocalFileStorage(@Value("${hrbank.storage.local.root-path}") Path root) {
    this.root = root;
  }

  @PostConstruct
  public void init() {
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public Long put(Long fileMetadataId, byte[] bytes) {
    Path filePath = resolvePath(fileMetadataId);

    if(Files.exists(filePath)){
      throw new IllegalArgumentException("File with key " + fileMetadataId + " already exists");
    }

    try(OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return fileMetadataId;
  }

  @Override
  public InputStream get(Long fileMetadataId) {
    Path filePath = resolvePath(fileMetadataId);
    if(Files.notExists(filePath)) {
      throw new NoSuchElementException("File with key " + fileMetadataId + " does not exist");
    }
    try{
      return Files.newInputStream(filePath);
    } catch (IOException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(Long fileMetadataId) {
    Path filePath = resolvePath(fileMetadataId);
    try {
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      throw new RuntimeException("파일 삭제 실패 - fileId: " + fileMetadataId, e);
    }
  }

  private Path resolvePath(Long fileMetaDataId) {
    return root.resolve(String.valueOf(fileMetaDataId));
  }
}
