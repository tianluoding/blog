package com.blog.controller;

import com.blog.resp.CommonResp;
import com.blog.resp.StatisticResp;
import com.blog.service.EbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/get-statistic")
    public CommonResp<List<StatisticResp>> getStatistic(){
        List<StatisticResp> statisticResp = ebookSnapshotService.getStatistic();
        return CommonResp.success(statisticResp);
    }

    @GetMapping("/get-30-statistic")
    public CommonResp<List<StatisticResp>> get30Statistic() {
        List<StatisticResp> statisticResp = ebookSnapshotService.get30Statistic();

        return CommonResp.success(statisticResp);
    }
}
