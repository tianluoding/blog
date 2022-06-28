package com.blog.job;

import com.blog.service.EbookSnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EbookSnapshotJob {
    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);

    @Autowired
    private EbookSnapshotService ebookSnapshotService;


    @Scheduled(cron = "0 0 23 * * ?")
    public void doSnapshot() {
        LOG.info("生成今日电子书快照开始");
        Long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        LOG.info("生成今日电子书快照结束，耗时: {}毫秒", System.currentTimeMillis() - start);
    }
}
