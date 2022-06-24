package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.domain.Ebook;
import com.blog.req.PageReq;
import com.blog.resp.CommonResp;
import com.blog.service.EbookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ebook")
@Slf4j
public class EbookController {
    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp<List<Ebook>> list() {
        List<Ebook> ebookList = ebookService.list();
        return CommonResp.success(ebookList);
    }

    @GetMapping("/page")
    public CommonResp<Page> page(@Valid PageReq pageReq) {
        Page pageInfo = new Page(pageReq.getPage(), pageReq.getPageSize());
        ebookService.page(pageInfo);
        return CommonResp.success(pageInfo);
    }

    @PostMapping("/save")
    public CommonResp<String> save(@RequestBody @Valid Ebook ebook) {

        ebookService.saveOrUpdate(ebook);
        return CommonResp.success("编辑电子书成功");

    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<String> remove(@PathVariable String id) {
        ebookService.removeById(id);
        return CommonResp.success("删除成功");
    }
}
