package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.blog.domain.Content;
import com.blog.domain.Ebook;
import com.blog.resp.CommonResp;
import com.blog.service.ContentService;
import com.blog.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private EbookService ebookService;

    @GetMapping("/{id}")
    public CommonResp<Content> getContent(@PathVariable Long id){
        UpdateWrapper<Ebook> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).setSql("view_count = view_count + 1");

        ebookService.update(updateWrapper);

        Content content = contentService.getById(id);

        return CommonResp.success(content);
    }

    @PostMapping
    public CommonResp<String> saveContent(@RequestBody Content content){
        contentService.saveOrUpdate(content);
        return CommonResp.success("保存成功");
    }
}
