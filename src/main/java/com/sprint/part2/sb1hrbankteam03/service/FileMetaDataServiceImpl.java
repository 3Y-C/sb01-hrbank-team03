package com.sprint.part2.sb1hrbankteam03.service;

import com.sprint.part2.sb1hrbankteam03.entity.FileCategory;
import com.sprint.part2.sb1hrbankteam03.entity.FileMetaData;
import com.sprint.part2.sb1hrbankteam03.repository.FileMetaDataRepository;
import com.sprint.part2.sb1hrbankteam03.stroage.FileStorage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Service
@RequiredArgsConstructor
public class FileMetaDataServiceImpl implements FileMetaDataService{

  private final FileMetaDataRepository fileMetaDataRepository;
  private final FileStorage fileStorage;


  @Override
  @Transactional
  public FileMetaData create(MultipartFile multipartFile, FileCategory fileCategory){

    if(multipartFile == null) return null;

    FileMetaData fileMetaData= new FileMetaData(multipartFile.getName(),
        multipartFile.getContentType(),
        multipartFile.getSize(),
        fileCategory);

    fileMetaDataRepository.save(fileMetaData);

    try {
      fileStorage.put(fileMetaData.getId(), multipartFile.getBytes());
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 중 오류 발생", e);
    }
    return fileMetaData;
  }

  //다운로드 응답 생성 책임
  @Override
  public ResponseEntity<?> download(Long fileMetaDataId) {
    FileMetaData fileMetaData = fileMetaDataRepository.findById(fileMetaDataId)
        .orElseThrow(() -> new NoSuchElementException(
            "FileMetaData with id " + fileMetaDataId + " not found"));

    InputStream inputStream = fileStorage.get(fileMetaData.getId());

    String encodedFileName = URLEncoder.encode(fileMetaData.getFileName(), StandardCharsets.UTF_8)
        .replaceAll("\\+", "%20");
    String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName;

    MediaType mediaType;
    try {
      mediaType = MediaType.parseMediaType(fileMetaData.getFileType());
    } catch (InvalidMediaTypeException e) {
      mediaType = MediaType.APPLICATION_OCTET_STREAM;
    }

    long fileSize = fileMetaData.getFileSize();
    long tenMB = 10 * 1024 * 1024;

    if (fileSize <= tenMB) {
      Resource resource = new InputStreamResource(inputStream);
      return ResponseEntity.ok()
          .contentType(mediaType)
          .contentLength(fileSize)
          .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
          .body(resource);
    } else {
      StreamingResponseBody responseBody = outputStream -> {
        byte[] buffer = new byte[8192];
        int bytesRead;
        try (inputStream) {
          while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
          }
          outputStream.flush();
        }
      };
      return ResponseEntity.ok()
          .contentType(mediaType)
          .contentLength(fileSize)
          .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
          .body(responseBody);
    }
  }

  @Override
  @Transactional
  public void delete(Long fileMetadataId) {
/*    FileMetaData fileMetaData = fileMetaDataRepository.findById(fileMetadataId)
        .orElseThrow(() -> new NoSuchElementException("FileMetaData with id " + fileMetadataId + " not found"));*/

    fileStorage.delete(fileMetadataId); //파일 먼저 삭제 (db를 먼저 삭제 후, 파일 삭제 실패하는 경우 -  롤백 문제 발생 가능)

/*    fileMetaDataRepository.delete(fileMetaData);*/
  }

}
