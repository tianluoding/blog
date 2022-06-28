package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.EbookSnapshot;
import com.blog.resp.StatisticResp;

import java.util.List;

public interface EbookSnapshotService extends IService<EbookSnapshot> {
   void genSnapshot();

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();
}
