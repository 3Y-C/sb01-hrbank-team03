package com.sprint.part2.sb1hrbankteam03.scheduler;

import com.sprint.part2.sb1hrbankteam03.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class BackupScheduler {

  private final BackupService backupService;


  @Value("${backup.scheduler.enabled:true}")
  private boolean schedulerEnabled;

  @Value("${backup.interval.minutes:60}")
  private int interval;

  //주기적으로 실행할 메서드에 적용한다.
  //fixedDelay: 이전 작업 완료 지정된 시간(밀리초) 후에 다음 작업 실행
  //밀리초 단위이므로 설정값(분)*1000 해준다.
  @Scheduled(fixedDelayString = "${backup.interval.milliseconds}")
  public void scheduledBackup() {
    if (schedulerEnabled) {//system 을 작업자로 지정하여 백업 실행한다.
      backupService.createBackup("system");
    }
  }
}
