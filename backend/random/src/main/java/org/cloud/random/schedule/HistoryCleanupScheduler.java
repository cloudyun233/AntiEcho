package org.cloud.random.schedule;


import org.cloud.random.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class HistoryCleanupScheduler {

    @Autowired
    private HistoryRepository historyRepository;

    @Scheduled(cron = "0 0 4 * * ?") // 每天凌晨4点执行
    public void cleanupOldHistory() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        historyRepository.deleteByTimestampBefore(thirtyDaysAgo);
    }
}
