package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.domain.Ebook;
import com.blog.resp.CommonResp;
import com.blog.service.EbookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
@Slf4j
public class EbookController {
    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp<List<Ebook>> list(){
        List<Ebook> ebookList = ebookService.list();
        return CommonResp.success(ebookList);
    }

    @GetMapping("/page")
    public  CommonResp<Page> page(int page, int pageSize) {
        Page pageInfo = new Page(page, pageSize);
        log.info("{}: {}", page, pageSize);
        //LambdaQueryWrapper<Ebook> queryWrapper = new LambdaQueryWrapper<>();
        ebookService.page(pageInfo);
        return CommonResp.success(pageInfo);
    }
}
